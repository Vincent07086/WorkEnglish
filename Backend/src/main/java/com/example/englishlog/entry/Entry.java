package com.example.englishlog.entry;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    long folderId;
    String title;
    @Column(length = 10000)
    String content;
    String imageUrl;
    Instant createdAt = Instant.now();
}
