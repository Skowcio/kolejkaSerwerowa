package com.example.demo.repository;

import com.example.demo.model.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueueRepository extends JpaRepository<QueueEntry, Long> {
    Optional<QueueEntry> findByName(String name);
    void deleteByName(String name);
}
