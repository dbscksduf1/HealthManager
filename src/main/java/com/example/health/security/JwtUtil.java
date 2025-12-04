package com.example.health.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 🔥 토큰 생성
    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        long expireTime = 1000L * 60 * 60 * 24; // 24시간

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireTime))
                .signWith(key)
                .compact();
    }

    // 🔥 토큰에서 username 가져오기
    public String getUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    // 🔥 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true; // 정상적인 경우
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 토큰 위조, 만료 등
        }
    }
}
