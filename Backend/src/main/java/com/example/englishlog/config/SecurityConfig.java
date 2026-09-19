package com.example.englishlog.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable()).cors(c -> c.configurationSource(req -> {
            CorsConfiguration x = new CorsConfiguration();
            x.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
            x.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            x.setAllowedHeaders(List.of("*"));
            return x;
        })).authorizeHttpRequests(a -> a.requestMatchers("/api/auth/**", "/api/skills", "/swagger/**", "/v3/api-docs/**", "/uploads/**").permitAll().anyRequest().permitAll());
        return http.build();
    }
}
