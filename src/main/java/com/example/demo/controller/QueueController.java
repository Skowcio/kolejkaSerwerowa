package com.example.demo.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/queue")
public class QueueController {
    private final List<String> queue = new LinkedList<>();
    private final List<String> breakList = new LinkedList<>();
    private final SimpMessagingTemplate messagingTemplate;

    public QueueController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/add")
    public String addToQueue(@RequestParam String name) {
        queue.add(name);
        messagingTemplate.convertAndSend("/topic/queue", queue);
        return name + " added to queue";
    }

    @GetMapping("/add-special")
    public String addAsSpecial(@RequestParam String name) {
        queue.add(0, name);
        messagingTemplate.convertAndSend("/topic/queue", queue);
        return name + " added as special to front of queue";
    }

    @GetMapping("/remove")
    public String removeFromQueue(@RequestParam String name) {
        if (queue.remove(name)) {
            messagingTemplate.convertAndSend("/topic/queue", queue);
            return name + " removed from queue";
        } else {
            return name + " not found in queue";
        }
    }

    @GetMapping("/removeFirst")
    public String removeFirst() {
        if (!queue.isEmpty()) {
            String removed = queue.remove(0);
            messagingTemplate.convertAndSend("/topic/queue", queue);
            return removed + " removed from front of queue";
        } else {
            return "Queue is empty";
        }
    }

    @GetMapping("/break/add")
    public String addToBreak(@RequestParam String name) {
        if (!breakList.contains(name)) {
            breakList.add(name);
            messagingTemplate.convertAndSend("/topic/break", breakList);
            return name + " added to break list";
        } else {
            return name + " is already on break";
        }
    }

    @GetMapping("/break/remove")
    public String removeFromBreak(@RequestParam String name) {
        if (breakList.remove(name)) {
            messagingTemplate.convertAndSend("/topic/break", breakList);
            return name + " removed from break list";
        } else {
            return name + " not found on break";
        }
    }

    @GetMapping("/break")
    public List<String> getBreakList() {
        return breakList;
    }

    @GetMapping
    public List<String> getQueue() {
        return queue;
    }

    // ✅ NOWE ENDPOINTY DLA FRONTENDU PRZY ŁADOWANIU STRONY

    @GetMapping("/all")
    public List<String> getAllQueue() {
        return queue;
    }

    @GetMapping("/break/all")
    public List<String> getAllBreak() {
        return breakList;
    }
}
