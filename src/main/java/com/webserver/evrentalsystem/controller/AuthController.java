package com.webserver.evrentalsystem.controller;

import com.webserver.evrentalsystem.model.dto.SigninRequest;
import com.webserver.evrentalsystem.model.dto.SigninResponse;
import com.webserver.evrentalsystem.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SigninRequest signinRequest, HttpServletResponse httpServletResponse) {
        SigninResponse signinResponse = authService.signIn(signinRequest, httpServletResponse);
        return ResponseEntity.ok(signinResponse);
    }

    @PostMapping(value = "/sign-out")
    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        authService.signOut(request, response);
    }
}
