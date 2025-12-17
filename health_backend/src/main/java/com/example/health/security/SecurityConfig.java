package com.example.health.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SecurityConfig
 * - Spring Security 전체 설정 파일
 * - JWT 기반 인증 방식 사용
 * - 세션 없이(stateless) 토큰으로만 인증 처리
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    /**
     * JWT 토큰을 검사하는 필터
     */
    private final JwtFilter jwtFilter;

    /**
     * CORS 설정
     * - 프론트엔드(로컬, Vercel)에서 오는 요청 허용
     * - 쿠키/Authorization 헤더 사용 가능하도록 설정
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // 허용할 프론트엔드 주소
                        .allowedOriginPatterns(
                                "http://localhost:*",
                                "https://*.vercel.app"
                        )
                        // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
                        .allowedMethods("*")
                        // 모든 헤더 허용
                        .allowedHeaders("*")
                        // 프론트에서 응답 헤더 확인 가능
                        .exposedHeaders("*")
                        // 인증 정보 포함 허용
                        .allowCredentials(true);
            }
        };
    }

    /**
     * Spring Security 핵심 설정
     * - 어떤 요청을 허용할지
     * - 어떤 요청은 인증이 필요한지 정의
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CSRF 비활성화 (JWT 방식이므로 필요 없음)
                .csrf(csrf -> csrf.disable())

                // 위에서 정의한 CORS 설정 적용
                .cors(cors -> {})

                // 세션 사용 안 함 (JWT는 Stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 기본 로그인 폼 비활성화
                .formLogin(form -> form.disable())

                // HTTP Basic 인증 비활성화
                .httpBasic(basic -> basic.disable())

                // 요청별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // Preflight 요청 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 🔥 인증 없이 접근 가능한 API
                        .requestMatchers("/ping").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/user/create").permitAll()
                        .requestMatchers("/health/**").permitAll()
                        .requestMatchers("/ai/**").permitAll()

                        // 나머지는 전부 인증 필요
                        .anyRequest().authenticated()
                )

                // JWT 필터를 Security 필터 체인에 추가
                // (UsernamePasswordAuthenticationFilter 전에 실행됨)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // 인증 / 권한 오류 처리
                .exceptionHandling(e -> e
                        // 인증 안 된 상태로 접근했을 때
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(401);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"인증이 필요합니다.\"}");
                        })
                        // 권한 없는 사용자가 접근했을 때
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(403);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"권한이 없습니다.\"}");
                        })
                );

        return http.build();
    }
}
