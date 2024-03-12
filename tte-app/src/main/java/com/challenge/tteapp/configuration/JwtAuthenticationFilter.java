package com.challenge.tteapp.configuration;

import com.challenge.tteapp.processor.JwtService;
import com.challenge.tteapp.processor.ValidationError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor

public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ValidationError validationError;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            final String token = getTokenFromRequest(request);
            final String email;
            final String role;
            final String endpoint;

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        email = jwtService.getUsernameFromToken(token);
        role = jwtService.getRoleFromToken(token);
        endpoint = request.getRequestURI();
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (jwtService.isTokenValid(token, userDetails)) {
                if (isAuthorized(role,endpoint)) {
                    UserRoleContext.setRole(role);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    String jsonResponse = "{\"error\": \"Access denied\"}";
                    response.getWriter().write(jsonResponse);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
        UserRoleContext.clear();
    }

    private boolean isAuthorized(String role,String endpoint) {
        if (endpoint.equals("/api/admin/auth")) {
            return role.equals("ADMIN");
        } else if (endpoint.startsWith("/api/product")) {
            return role.equals("ADMIN") || role.equals("employee");
        } else if (endpoint.startsWith("/api/category")) {
            return role.equals("ADMIN") || role.equals("employee");
        } else {
            return false;
        }
    }

    private String getTokenFromRequest(HttpServletRequest request){
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return  authHeader.substring(7);
        }
        return null;
    }


}
