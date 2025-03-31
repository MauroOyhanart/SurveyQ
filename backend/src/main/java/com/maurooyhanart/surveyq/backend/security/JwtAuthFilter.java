package com.maurooyhanart.surveyq.backend.security;

import com.maurooyhanart.surveyq.backend.user.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            System.out.println("JwtAuthFilter applied for: " + request.getRequestURI());
            if (shouldNotFilter(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("JwtAuthFilter applied for: " + request.getRequestURI());
            String jwt = parseJwt(request);
            if (jwt != null) {
                if (jwtUtils.validateJwt(jwt)) {
                    String email = jwtUtils.getEmailFromJwt(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    List<GrantedAuthority> authorities = extractAuthoritiesFromJwt(jwt);
                    userDetails = new User(email, "------", authorities); //I could make a CustomUserDetails to improve this but this works for now
                    // Set authentication in SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            )
                    );
                } else {
                    System.out.println("Invalid JWT");
                }
            } else {
                System.out.println("JWT not present");
            }
        } catch (Exception e) {
            logger.error("Cannot authenticate user: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private List<GrantedAuthority> extractAuthoritiesFromJwt(String jwt) {
        Claims claims = jwtUtils.getClaimsFromJwt(jwt);
        List<String> roles = claims.get("roles", List.class);

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))  // Prefix "ROLE_" to each role
                .collect(Collectors.toList());
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }
}
