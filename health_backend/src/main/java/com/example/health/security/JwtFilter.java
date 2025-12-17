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

/**
 * JwtFilter
 * - 모든 HTTP 요청에 대해 JWT 토큰을 검사하는 보안 필터
 * - 인증이 필요한 요청인지 판단하고
 * - 유효한 JWT가 존재할 경우 SecurityContext에 인증 정보 설정
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * JWT 토큰 생성/검증 유틸 클래스
     */
    private final JwtUtil jwtUtil;

    /**
     * 생성자 주입
     * @param jwtUtil JWT 유틸 클래스
     */
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 요청당 한 번 실행되는 JWT 필터 로직
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 URI 확인
        String path = request.getRequestURI();


        // - 헬스 체크
        // - 로그인
        // - 회원가입
        if (path.equals("/ping")
                || path.startsWith("/user/login")
                || path.startsWith("/user/create")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 JWT 추출
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Bearer 토큰 형식 확인
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                // 토큰에서 사용자명 추출
                username = jwtUtil.getUsername(token);
            } catch (Exception e) {
                // 토큰 파싱 실패 시 접근 거부
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\":\"권한이 없습니다.\"}");
                return;
            }
        }

        // 사용자명이 존재하고 아직 인증되지 않은 경우
        // + 토큰이 유효한 경우에만 인증 처리
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null &&
                jwtUtil.validateToken(token)) {

            // Spring Security 인증 객체 생성
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, null);

            // 요청 정보를 인증 객체에 설정
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            // SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
