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

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * 🔥 CORS 설정 — 배포/로컬 둘 다 Authorization 헤더 정상 전달되게 하는 핵심
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")

                        // 🔥 allowedOrigins → allowedOriginPatterns 로 변경
                        //    vercel의 모든 하위 도메인, localhost 포트 전부 허용
                        .allowedOriginPatterns(
                                "http://localhost:*",
                                "https://*.vercel.app"
                        )

                        // 모든 메서드 허용
                        .allowedMethods("*")

                        // 모든 헤더 허용 (Authorization 포함)
                        .allowedHeaders("*")

                        // 프론트에서 Authorization 헤더 읽을 수 있도록 허용
                        .exposedHeaders("*")

                        // JWT 인증 위해 credentials 必
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })   // 🔥 반드시 corsConfigurer() 와 연결됨

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/user/create").permitAll()
                        .requestMatchers("/health/**").permitAll()
                        .requestMatchers("/ai/**").permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(401);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"인증이 필요합니다.\"}");
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(403);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"권한이 없습니다.\"}");
                        })
                );

        return http.build();
    }
}
