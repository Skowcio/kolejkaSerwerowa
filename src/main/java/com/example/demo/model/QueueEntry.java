
package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class QueueEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
// albo tu jest problem
//    private int position; // dodaj to pole

    public QueueEntry() {
    }

    public QueueEntry(String name) {
        this.name = name;

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public int getPosition() {
//        return position;
//    }
//
//    public void setPosition(int position) {
//        this.position = position;
//    }

    public void setName(String name) {
        this.name = name;
    }
}
