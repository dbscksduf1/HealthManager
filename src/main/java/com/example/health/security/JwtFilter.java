package com.example.health.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 🔥 1) 로그인/회원가입은 JWT 검사 제외
        String path = request.getRequestURI();
        if (path.startsWith("/user/login") || path.startsWith("/user/create")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 2) JWT 인증 처리
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.getUsername(token);
            } catch (Exception e) {
                // 토큰 문제 있을 때 403 리턴
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"권한이 없습니다.\"}");
                return;
            }
        }

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null &&
                jwtUtil.validateToken(token)) {

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, null);

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
