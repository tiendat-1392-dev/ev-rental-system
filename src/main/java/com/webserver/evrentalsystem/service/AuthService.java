package com.webserver.evrentalsystem.service;

import com.webserver.evrentalsystem.model.dto.SigninRequest;
import com.webserver.evrentalsystem.model.dto.SigninResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    SigninResponse signIn(SigninRequest signinRequest, HttpServletResponse httpServletResponse);
    void signOut(HttpServletRequest request, HttpServletResponse response);
}
