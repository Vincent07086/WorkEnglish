package com.example.englishlog.entry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findAllByOrderByCreatedAtDesc();

    List<Entry> findByFolderIdOrderByCreatedAtDesc(long folderId);
}
