package com.example.health.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:5173",
                                "http://localhost:3000",
                                "https://health-manager-frontend-henna.vercel.app"
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .exposedHeaders("*")     // ★ JWT 헤더 브라우저에서 읽을 수 있게 꼭 필요
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})    // ★ 반드시 필요 — WebMvcConfigurer와 연결됨
                .sessionManagement(session -> session.disable())
                .securityContext(context -> context.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // preflight 허용
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
