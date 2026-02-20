package com.kodbank.security;

import com.kodbank.entity.UserToken;
import com.kodbank.repository.UserTokenRepository;
import com.kodbank.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserTokenRepository userTokenRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {
        
        final String requestPath = request.getRequestURI();
        
        // Skip JWT validation for static HTML pages, favicon, and auth endpoints
        if (requestPath.endsWith(".html") || requestPath.equals("/") || 
            requestPath.equals("/favicon.ico") ||
            requestPath.startsWith("/api/auth") ||
            requestPath.startsWith("/css/") || 
            requestPath.startsWith("/js/") ||
            requestPath.startsWith("/images/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Try to get token from Authorization header first, then Cookie
        String jwt = null;
        String authHeader = request.getHeader("Authorization");
        
        // Check Bearer token in Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        } else {
            // Check cookie
            String cookieHeader = request.getHeader("Cookie");
            if (cookieHeader != null && cookieHeader.contains("token=")) {
                String tokenValue = cookieHeader.substring(cookieHeader.indexOf("token=") + 6);
                if (tokenValue.contains(";")) {
                    tokenValue = tokenValue.substring(0, tokenValue.indexOf(";"));
                }
                jwt = tokenValue.trim();
            }
        }
        
        if (jwt == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"No token provided\"}");
            return;
        }
        
        try {
            // Validate token in database
            Optional<UserToken> tokenOptional = userTokenRepository.findByToken(jwt);
            if (tokenOptional.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid token\"}");
                return;
            }
            
            UserToken userToken = tokenOptional.get();
            if (userToken.getExpiry().isBefore(LocalDateTime.now())) {
                userTokenRepository.delete(userToken);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token expired\"}");
                return;
            }
            
            // Extract username from JWT
            String username = jwtService.extractUsername(jwt);
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtService.isTokenValid(jwt)) {
                    String role = jwtService.extractRole(jwt);
                    
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid token\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}
