package com.example.englishlog.entry;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/entries")
@CrossOrigin
public class EntryController {
    final EntryRepository repo;

    EntryController(EntryRepository r) {
        repo = r;
    }

    @GetMapping
    List<Entry> all(@RequestParam(required = false) Long folderId) {
        return folderId == null ? repo.findAllByOrderByCreatedAtDesc() : repo.findByFolderIdOrderByCreatedAtDesc(folderId);
    }

    @PostMapping
    Entry create(@RequestBody Entry e) {
        e.setCreatedAt(java.time.Instant.now());
        return repo.save(e);
    }
}
