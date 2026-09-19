package com.example.englishlog.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.englishlog.config.JwtService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(UserRepository u, PasswordEncoder e, JwtService j) {
        users = u;
        encoder = e;
        jwt = j;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest r) {
        if (users.existsByEmail(r.email())) return ResponseEntity.status(409).body("Email exists");
        User u = users.save(User.builder().email(r.email()).name(r.name() == null ? r.email() : r.name()).passwordHash(encoder.encode(r.password())).build());
        return ResponseEntity.ok(new AuthResponse(jwt.create(u), new UserDto(u)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest r) {
        return users.findByEmail(r.email()).filter(u -> encoder.matches(r.password(), u.getPasswordHash())).<ResponseEntity<?>>map(u -> ResponseEntity.ok(new AuthResponse(jwt.create(u), new UserDto(u)))).orElseGet(() -> ResponseEntity.status(401).build());
    }

    @GetMapping("/google")
    public ResponseEntity<?> google() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Configure GOOGLE_CLIENT_ID and GOOGLE_CLIENT_SECRET, then connect Spring Security OAuth2 login.");
    }

    public record RegisterRequest(@Email @NotBlank String email, @Size(min = 8) String password, String name) {
    }

    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {
    }

    public record AuthResponse(String token, UserDto user) {
    }

    public record UserDto(Long id, String email, String name) {
        UserDto(User u) {
            this(u.getId(), u.getEmail(), u.getName());
        }
    }
}
