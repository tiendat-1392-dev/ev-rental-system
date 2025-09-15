package com.webserver.evrentalsystem.filter;

import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.*;
import com.webserver.evrentalsystem.exception.Error;
import com.webserver.evrentalsystem.exception.ErrorResponse;
import com.webserver.evrentalsystem.repository.BlockedSessionRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.AuthService;
import com.webserver.evrentalsystem.utils.CookieUtils;
import com.webserver.evrentalsystem.utils.JwtUtils;
import com.webserver.evrentalsystem.utils.Logger;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${JWT_ACCESS_TOKEN_EXPIRATION_MS}")
    private long jwtAccessTokenExpirationMs;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlockedSessionRepository blockedSessionRepository;

    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            response.setCharacterEncoding("UTF-8");

            String accessToken = CookieUtils.getCookieValue(CookieUtils.ACCESS_TOKEN, request);
            String refreshToken = CookieUtils.getCookieValue(CookieUtils.REFRESH_TOKEN, request);

            String url = request.getRequestURI();
            Logger.printf("url: " + url);

            if (accessToken == null || accessToken.isEmpty() || !jwtUtils.validateJwtToken(accessToken)) {
                // refresh access token
                boolean isSuccessful = refreshAccessToken(refreshToken, response);
                Logger.printf("Refresh access token successfully: " + isSuccessful);
                if (!isSuccessful) {
                    return;
                }
            }

            String userName = jwtUtils.getUserNameFromJwtToken(accessToken);
            String role = jwtUtils.getRoleFromJwtToken(accessToken);
            request.setAttribute("userName", userName);
            request.setAttribute("role", role);

            // check role
            if (url.contains("/moderator/")) {
                if (!role.equals(Role.MODERATOR.getValue()) && !role.equals(Role.ADMIN.getValue())) {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write(new ErrorResponse(HttpStatus.FORBIDDEN.value(), Error.PermissionDenied.getValue(), "Bạn không có quyền thực hiện hành động này").toJson());
                    return;
                }
            } else if (url.contains("/admin/")) {
                if (!role.equals(Role.ADMIN.getValue())) {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write(new ErrorResponse(HttpStatus.FORBIDDEN.value(), Error.PermissionDenied.getValue(), "Bạn không có quyền thực hiện hành động này").toJson());
                    return;
                }
            }

            // check if user is blocked
            boolean isBlocked = blockedSessionRepository.isBlocked(userName);
            if (isBlocked) {
                // remove access token and refresh token
                clearTokenCookie(response);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), Error.UserIsBlocked.getValue(), "Tài khoản của bạn đã bị khóa!").toJson());
                return;
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            Logger.printf("Error in JwtAuthenticationFilter: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), Error.InternalServer.getValue(), "Lỗi hệ thống!").toJson());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String url = request.getRequestURL().toString();
        return isResourceUrl(url);
    }

    private boolean isResourceUrl(String url) {
        boolean isResourceUrl = false;
        List<String> resourceRequests = List.of(
                "/auth/", "/public/");

        if (url.contains("/auth/sign-out")) {
            return false;
        }

        for (String resourceRequest : resourceRequests) {
            if (url.contains(resourceRequest)) {
                isResourceUrl = true;
                break;
            }
        }
        return isResourceUrl;
    }

    private boolean refreshAccessToken(String refreshToken, HttpServletResponse response) throws IOException {
        if (refreshToken == null || refreshToken.isEmpty()) {
            Logger.printf("Refresh token không hợp lệ!");
            clearTokenCookie(response);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(new ErrorResponse(HttpStatus.FORBIDDEN.value(), Error.ExpiredRefreshToken.getValue(), "Refresh token không hợp lệ!").toJson());
            return false;
        }

        if (!jwtUtils.validateJwtToken(refreshToken)) {
            Logger.printf("Refresh token đã hết hạn!");
            clearTokenCookie(response);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(new ErrorResponse(HttpStatus.FORBIDDEN.value(), Error.ExpiredRefreshToken.getValue(), "Refresh token không hợp lệ!").toJson());
            return false;
        }

        String userName = jwtUtils.getUserNameFromJwtToken(refreshToken);

        User user = userRepository.findByUserName(userName);

        if (user == null) {
            Logger.printf("Khong tim thay user!");
            clearTokenCookie(response);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(new ErrorResponse(HttpStatus.FORBIDDEN.value(), Error.ExpiredRefreshToken.getValue(), "Refresh token không hợp lệ!").toJson());
            return false;
        }

        if (!refreshToken.equals(user.getRefreshToken())) {
            Logger.printf("Refresh token khac trong database!");
            clearTokenCookie(response);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(new ErrorResponse(HttpStatus.FORBIDDEN.value(), Error.ExpiredRefreshToken.getValue(), "Refresh token không hợp lệ!").toJson());
            return false;
        }

        Logger.printf("Generate new access token!");

        String newAccessToken = jwtUtils.generateJwtAccessToken(user);

        // save access token to cookie
        ResponseCookie accessTokenCookie = CookieUtils.createCookie(CookieUtils.ACCESS_TOKEN, newAccessToken, (int) (jwtAccessTokenExpirationMs / 1000));
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        return true;
    }

    private void clearTokenCookie(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = CookieUtils.createCookie(CookieUtils.ACCESS_TOKEN, null, 0);
        ResponseCookie refreshTokenCookie = CookieUtils.createCookie(CookieUtils.REFRESH_TOKEN, null, 0);
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}
