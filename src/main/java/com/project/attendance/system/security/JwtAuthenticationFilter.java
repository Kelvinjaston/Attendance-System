package com.project.attendance.system.security;

import com.project.attendance.system.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired private JwtTokenProvider tokenProvider;
    @Autowired private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = req.getRequestURI();
        String authHeader = req.getHeader("Authorization");

        if (path.startsWith("/api/registration") || path.startsWith("/api/lecturer")) {
            System.out.println("--- FILTER DEBUG --- Request Path: " + path);
            System.out.println("--- FILTER DEBUG --- Auth Header Present: " + (authHeader != null));
            if (authHeader != null) {
                System.out.println("--- FILTER DEBUG --- Header Value: " + authHeader);
            }
        }

        String token = extractToken(req);

        if (token != null) {
            if (tokenProvider.validateToken(token)) {
                try {
                    String username = tokenProvider.extractUsername(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    System.out.println("--- FILTER SUCCESS --- Authenticated User: " + username);
                    System.out.println("--- FILTER SUCCESS --- Roles found: " + userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                } catch (Exception e) {
                    System.out.println("--- FILTER ERROR --- Authentication failed: " + e.getMessage());
                }
            } else {
                System.out.println("--- FILTER FAILURE --- Token was found but it is INVALID (Check Secret Key or Expiry)");
            }
        }

        chain.doFilter(req, res);
    }

    private String extractToken(HttpServletRequest req) {
        String bearer = req.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}