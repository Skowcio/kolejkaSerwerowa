package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class QueueEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public QueueEntry() {}

    public QueueEntry(String name) {
        this.name = name;
    }

    // Gettery i settery
}