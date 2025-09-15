package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.BlockedSession;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.*;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.UserIsBlockedException;
import com.webserver.evrentalsystem.exception.UserNotFoundException;
import com.webserver.evrentalsystem.model.dto.SigninRequest;
import com.webserver.evrentalsystem.model.dto.SigninResponse;
import com.webserver.evrentalsystem.model.mapping.UserMapping;
import com.webserver.evrentalsystem.repository.BlockedSessionRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.AuthService;
import com.webserver.evrentalsystem.service.validation.UserValidation;
import com.webserver.evrentalsystem.utils.CommonUtils;
import com.webserver.evrentalsystem.utils.CookieUtils;
import com.webserver.evrentalsystem.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${JWT_ACCESS_TOKEN_EXPIRATION_MS}")
    private long jwtAccessTokenExpirationMs;

    @Value("${JWT_REFRESH_TOKEN_EXPIRATION_MS}")
    private long jwtRefreshTokenExpirationMs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapping userMapping;

    @Autowired
    private BlockedSessionRepository blockedSessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public SigninResponse signIn(SigninRequest signinRequest, HttpServletResponse httpServletResponse) {
        String userName = signinRequest.getUserName();
        String password = signinRequest.getPassword();

        if (userName.isEmpty() || password.isEmpty()) {
            throw new InvalidateParamsException("Tên đăng nhập và mật khẩu không được để trống");
        }

        User user = userRepository.findByUserName(userName);

        if (user == null) {
            throw new UserNotFoundException("Người dùng không tồn tại");
        }

        String hashedPassword = user.getPassword();

        if (!passwordEncoder.matches(password, hashedPassword)) {
            throw new InvalidateParamsException("Mật khẩu không chính xác");
        }

        // check if user is blocked
        BlockedSession blockedSession = blockedSessionRepository.findAndCheckIfUserIsBlocked(user);
        if (blockedSession != null) {
            throw new UserIsBlockedException(CommonUtils.generateBlockMessage(blockedSession));
        }

        String accessToken = jwtUtils.generateJwtAccessToken(user);
        String refreshToken = jwtUtils.generateJwtRefreshToken(user);

        // save refresh token to database
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setUserInfo(userMapping.toUserDto(user));

        // save access token to cookie
        ResponseCookie accessTokenCookie = CookieUtils.createCookie(CookieUtils.ACCESS_TOKEN, accessToken, (int) (jwtAccessTokenExpirationMs / 1000));
        ResponseCookie refreshTokenCookie = CookieUtils.createCookie(CookieUtils.REFRESH_TOKEN, refreshToken, (int) (jwtRefreshTokenExpirationMs / 1000));
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return signinResponse;
    }

    @Override
    public void signOut(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        User user = UserValidation.validateUser(userRepository, request);

        user.setRefreshToken(null);
        userRepository.save(user);

        // remove access token and refresh token from cookie
        ResponseCookie accessTokenCookie = CookieUtils.createCookie(CookieUtils.ACCESS_TOKEN, null, 0);
        ResponseCookie refreshTokenCookie = CookieUtils.createCookie(CookieUtils.REFRESH_TOKEN, null, 0);
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}
