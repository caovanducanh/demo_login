package com.example.demologin.config;

import com.example.demologin.dto.response.ResponseObject;
import com.example.demologin.entity.User;
import com.example.demologin.exception.exceptions.InvalidTokenException;
import com.example.demologin.exception.exceptions.UnauthorizedException;
import com.example.demologin.service.TokenService;
import com.example.demologin.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class Filter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final JwtUtil jwtUtil;
    private final PublicEndpointHandlerMapping publicEndpointHandlerMapping;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            if (isPermitted(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = getToken(request);
            if (token == null) {
                throw new UnauthorizedException("Authentication token is missing!");
            }

            String username = jwtUtil.extractUsername(token);
            if (username == null || username.isBlank()) {
                throw new InvalidTokenException("Authentication token is invalid!");
            }

            User user = tokenService.getUserByToken(token);
            if (user == null) {
                throw new UnauthorizedException("User not found for the provided token!");
            }

            if (!jwtUtil.validateTokenWithJtiCheck(token, user)) {
                throw new InvalidTokenException("Authentication token is invalid or revoked!");
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            ResponseObject respObj = new ResponseObject(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), null);
            response.getWriter().write(new ObjectMapper().writeValueAsString(respObj));
        }
    }

    private boolean isPermitted(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String uri = request.getRequestURI();

        // Check public endpoints from annotations
        List<String> annotatedPublicEndpoints = publicEndpointHandlerMapping.getPublicEndpoints();
        boolean isAnnotatedPublic = annotatedPublicEndpoints.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));

        if (isAnnotatedPublic) {
            return true;
        }


    // System public endpoints
    List<String> systemPublicEndpoints = List.of(
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/webjars/**",
        "/login/oauth2/code/**",
        "/oauth2/authorization/**",
        "/graphiql"
    );

        return systemPublicEndpoints.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }
}