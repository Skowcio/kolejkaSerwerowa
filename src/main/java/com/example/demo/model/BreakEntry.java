package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class BreakEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public BreakEntry() {}

    public BreakEntry(String name) {
        this.name = name;
    }

    // Gettery i settery
}
