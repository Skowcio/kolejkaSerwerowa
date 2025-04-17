package com.example.demo.repository;

import com.example.demo.model.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.util.Optional;

public interface QueueRepository extends JpaRepository<QueueEntry, Long> {

    Optional<QueueEntry> findByName(String name);


    // tu jest problem albo
//    Optional<QueueEntry> findByPosition(int position);
//
//    List<QueueEntry> findAllByOrderByPositionAsc();

    @Transactional
    @Modifying
    @Query("DELETE FROM QueueEntry q WHERE q.name = :name")
    void deleteByName(String name);
}
