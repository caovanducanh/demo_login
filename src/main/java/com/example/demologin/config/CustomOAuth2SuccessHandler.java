package com.example.demologin.config;

import com.example.demologin.dto.response.LoginResponse;
import com.example.demologin.entity.User;
import com.example.demologin.enums.ActivityType;
import com.example.demologin.repository.UserRepository;
import com.example.demologin.service.AuthenticationService;
import com.example.demologin.service.BranchService;
import com.example.demologin.service.UserActivityLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {


    private final AuthenticationService authenticationService;

    private final BranchService branchService;

    private final UserActivityLogService userActivityLogService;

    private final UserRepository userRepository;

    @Value("${frontend.url.base}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        
        // Get branch code from session or request parameter
        String branchCode = request.getParameter("branch");
        if (branchCode == null) {
            branchCode = (String) request.getSession().getAttribute("selectedBranch");
        }
        
        if (email == null) {
            response.sendRedirect(frontendUrl + "login?error=missing_email");
            return;
        }
        
        if (branchCode == null) {
            response.sendRedirect(frontendUrl + "login?error=missing_branch_selection");
            return;
        }
        
        try {
            // Validate email domain for selected branch
            if (!branchService.validateEmailForBranch(email, branchCode)) {
                throw new RuntimeException("Email domain not allowed for selected branch");
            }
            
            LoginResponse userResponse = authenticationService.getUserResponse(email, name, branchCode);
            
            // Keep the full token with signature (same as regular login)
            String token = userResponse.getToken();
            
            User user = userRepository.findByEmail(email).orElse(null);
            userActivityLogService.logUserActivity(user, ActivityType.LOGIN_ATTEMPT, 
                "OAuth2 login successful via " + getProviderName(authentication) + " for branch: " + branchCode);
            
            String redirectUrl = frontendUrl + "login?token=" + token + "&refreshToken=" + userResponse.getRefreshToken();
            response.sendRedirect(redirectUrl);
            
        } catch (Exception e) {
            // Log failed OAuth2 login attempt  
            User user = null;
            try {
                user = userRepository.findByEmail(email).orElse(null);
            } catch (Exception ignored) {
                // User might not exist
            }
            
            userActivityLogService.logUserActivity(user, ActivityType.LOGIN_ATTEMPT, 
                "OAuth2 login failed: " + e.getMessage());
            
            response.sendRedirect(
                    frontendUrl + "login?error=" + e.getMessage().replace(" ", "_"));
        }
    }
    
    private String getProviderName(Authentication authentication) {
        // Extract provider name from OAuth2 authentication
        if (authentication != null && authentication.getName() != null) {
            String authName = authentication.getName().toLowerCase();
            if (authName.contains("google")) {
                return "Google";
            } else if (authName.contains("facebook")) {
                return "Facebook";
            }
        }
        return "OAuth2 Provider";
    }
}