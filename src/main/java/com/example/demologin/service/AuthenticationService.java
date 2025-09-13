package com.example.demologin.service;

import com.example.demologin.dto.request.login.FacebookLoginRequest;
import com.example.demologin.dto.request.login.GoogleLoginRequest;
import com.example.demologin.dto.request.login.LoginRequest;
import com.example.demologin.dto.request.user.UserRegistrationRequest;
import com.example.demologin.dto.response.LoginResponse;
import com.example.demologin.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService {
    LoginResponse register(UserRegistrationRequest request);
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse authenticateWithGoogle(GoogleLoginRequest request);
    LoginResponse authenticateWithOAuth2FromAuthentication(org.springframework.security.core.Authentication authentication);
    LoginResponse authenticateWithFacebook(FacebookLoginRequest request);
    LoginResponse getUserResponse(String email, String name);
    LoginResponse getUserResponse(String email, String name, String branchCode);
    void handleOAuth2Failure();
}
