package com.example.englishlog.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/uploads")
@CrossOrigin
public class UploadController {
    @Value("${app.upload-dir}")
    String dir;

    @PostMapping
    public Map<String, String> upload(@RequestParam MultipartFile file) throws Exception {
        Files.createDirectories(Path.of(dir));
        String name = UUID.randomUUID() + "_" + Path.of(Objects.requireNonNull(file.getOriginalFilename())).getFileName();
        Files.copy(file.getInputStream(), Path.of(dir, name), StandardCopyOption.REPLACE_EXISTING);
        return Map.of("url", "/uploads/" + name);
    }
}
