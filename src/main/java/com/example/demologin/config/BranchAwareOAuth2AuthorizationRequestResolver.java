package com.example.demologin.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class BranchAwareOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    
    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    public BranchAwareOAuth2AuthorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request);
        
        if (authorizationRequest != null) {
            // Store branch parameter in session for later use in success handler
            String branchCode = request.getParameter("branch");
            if (branchCode != null) {
                request.getSession().setAttribute("selectedBranch", branchCode);
            }
        }
        
        return authorizationRequest;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
        
        if (authorizationRequest != null) {
            // Store branch parameter in session for later use in success handler
            String branchCode = request.getParameter("branch");
            if (branchCode != null) {
                request.getSession().setAttribute("selectedBranch", branchCode);
            }
        }
        
        return authorizationRequest;
    }
}
