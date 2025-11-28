package com.example.health.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
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
                                "https://healthmanager-backend.onrender.com",
                                "https://health-manager-frontend-henna.vercel.app"
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session -> session.disable())
                .securityContext(context -> context.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/user/login", "/user/create").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e ->
                        e
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

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> null;
    }
}
