package com.example.englishlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EnglishLogApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnglishLogApplication.class, args);
    }
}
