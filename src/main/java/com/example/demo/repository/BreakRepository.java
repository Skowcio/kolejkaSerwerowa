package com.example.demo.repository;

import com.example.demo.model.BreakEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BreakRepository extends JpaRepository<BreakEntry, Long> {

    Optional<BreakEntry> findByName(String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM BreakEntry b WHERE b.name = :name")
    void deleteByName(String name);
}
