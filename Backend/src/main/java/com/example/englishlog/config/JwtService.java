package com.example.englishlog.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.example.englishlog.auth.User;

@Service
public class JwtService {
    @Value("${app.jwt-secret}")
    private String secret;

    public String create(User u) {
        return Jwts.builder().subject(String.valueOf(u.getId())).claim("email", u.getEmail()).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 28800000)).signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).compact();
    }
}
