package com.example.englishlog.folder;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/folders")
@CrossOrigin
public class FolderController {
    final FolderRepository repo;

    FolderController(FolderRepository r) {
        repo = r;
    }

    @GetMapping
    List<Folder> all() {
        return repo.findAll();
    }

    @PostMapping
    Folder create(@RequestBody Folder f) {
        return repo.save(f);
    }
}
