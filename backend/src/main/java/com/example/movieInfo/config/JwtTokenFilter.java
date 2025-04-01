package com.example.movieInfo.config;

import com.example.movieInfo.service.UserService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final UserService userService;

    @Autowired
    public JwtTokenFilter(JwtConfig jwtConfig, UserService userService) {
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Ignoră endpoint-urile publice cu noile rute
        if (path.equals("/auth/register") ||
                path.equals("/auth/login") ||
                path.startsWith("/static/") ||
                path.equals("/favicon.ico") ||
                path.startsWith("/icons/") ||
                path.equals("/api/movies/recommendations") ||
                path.equals("/api/movies/search") ||
                path.equals("/api/movies/genres") ||
                path.matches("/api/movies/\\d+") || // ID numeric
                path.matches("/api/movies/[a-fA-F0-9-]{36}")) { // UUID
            chain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        if (token != null && validateToken(token)) {
            String username = getUsernameFromToken(token);
            UserDetails userDetails = userService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getSecretKey().getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("JWT validation failed: " + e.getMessage());
            return false;
        }
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKey().getBytes()) // Folosim jwtConfig
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
