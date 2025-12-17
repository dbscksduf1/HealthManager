package com.example.health.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtUtil
 * - 로그인 시 JWT 토큰을 만들어주고
 * - 요청이 들어올 때 토큰이 정상인지 확인하는 클래스
 * - "이 요청이 누구 요청인지"를 판단하기 위해 사용됨
 */
@Component
public class JwtUtil {

    /**
     * JWT 서명에 사용되는 비밀 키
     * - HS256 방식 사용
     * - 서버가 실행될 때 한 번 생성됨
     */
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * JWT 토큰 생성 메서드
     * - 로그인 성공 시 호출됨
     * - 토큰 안에 username을 담아서 반환
     */
    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        long expireTime = 1000L * 60 * 60 * 24; // 토큰 유효시간: 24시간

        return Jwts.builder()
                .setSubject(username)                 // 토큰 주인 (username)
                .setIssuedAt(new Date(now))           // 토큰 발급 시간
                .setExpiration(new Date(now + expireTime)) // 토큰 만료 시간
                .signWith(key)                        // 위조 방지를 위한 서명
                .compact();
    }

    /**
     * JWT 토큰에서 username 꺼내는 메서드
     * - 필터나 컨트롤러에서 사용
     * - 토큰이 이상하면 null 반환
     */
    public String getUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)               // 서명 키 설정
                    .build()
                    .parseClaimsJws(token)            // 토큰 해석
                    .getBody()
                    .getSubject();                    // 저장된 username 반환
        } catch (Exception e) {
            // 토큰이 위조됐거나 만료된 경우
            return null;
        }
    }

    /**
     * JWT 토큰이 유효한지 확인하는 메서드
     * - 토큰 위조 여부
     * - 토큰 만료 여부 검사
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)               // 서명 키 설정
                    .build()
                    .parseClaimsJws(token);           // 파싱 성공하면 정상 토큰

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 잘못됐거나 만료된 경우
            return false;
        }
    }
}
