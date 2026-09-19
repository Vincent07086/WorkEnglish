package com.example.englishlog.skill;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin
public class SkillController {
    @GetMapping
    public List<Map<String, String>> all() {
        return List.of(Map.of("id", "rest", "name", "REST API / HTTP / JSON", "file", "entry/EntryController.java", "why", "Darwin integration JD high-frequency skill"), Map.of("id", "spring", "name", "Spring DI / MVC / Validation", "file", "auth/AuthController.java", "why", "Java backend interview foundation"), Map.of("id", "jpa", "name", "JPA / SQL / PostgreSQL", "file", "entry/Entry.java", "why", "Data modelling and persistence"), Map.of("id", "security", "name", "JWT / BCrypt / OAuth2", "file", "config/SecurityConfig.java", "why", "Authentication and security"), Map.of("id", "upload", "name", "Multipart image upload", "file", "upload/UploadController.java", "why", "English image record feature"), Map.of("id", "deploy", "name", "Docker / OpenAPI / configuration", "file", "pom.xml,application.yml", "why", "Australian JD delivery skills"));
    }
}
