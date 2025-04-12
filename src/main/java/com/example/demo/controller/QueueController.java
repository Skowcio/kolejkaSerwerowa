package com.example.demo.controller;

import com.example.demo.model.BreakEntry;
import com.example.demo.model.QueueEntry;
import com.example.demo.repository.BreakRepository;
import com.example.demo.repository.QueueRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/queue")
public class QueueController {

    private final QueueRepository queueRepository;
    private final BreakRepository breakRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public QueueController(QueueRepository queueRepository, BreakRepository breakRepository, SimpMessagingTemplate messagingTemplate) {
        this.queueRepository = queueRepository;
        this.breakRepository = breakRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // === KOLEJKA ===

    @GetMapping("/add")
    public String addToQueue(@RequestParam String name) {
        queueRepository.save(new QueueEntry(name));
        sendQueueUpdate();
        return name + " added to queue";
    }

    @GetMapping("/remove")
    public String removeFromQueue(@RequestParam String name) {
        queueRepository.deleteByName(name);
        sendQueueUpdate();
        return name + " removed from queue";
    }

    @GetMapping("/removeFirst")
    public String removeFirst() {
        List<QueueEntry> all = queueRepository.findAll();
        if (!all.isEmpty()) {
            QueueEntry first = all.get(0);
            queueRepository.delete(first);
            sendQueueUpdate();
            return first.getName() + " removed from front of queue";
        } else {
            return "Queue is empty";
        }
    }

    @GetMapping("/all")
    public List<QueueEntry> getAllQueue() {
        return queueRepository.findAll();
    }

    // === PRZERWA ===

    @GetMapping("/break/add")
    public String addToBreak(@RequestParam String name) {
        if (breakRepository.findByName(name).isEmpty()) {
            breakRepository.save(new BreakEntry(name));
            sendBreakUpdate();
            return name + " added to break list";
        } else {
            return name + " is already on break";
        }
    }

    @GetMapping("/break/remove")
    public String removeFromBreak(@RequestParam String name) {
        breakRepository.deleteByName(name);
        sendBreakUpdate();
        return name + " removed from break list";
    }

    @GetMapping("/break/all")
    public List<BreakEntry> getAllBreak() {
        return breakRepository.findAll();
    }

    // === WYSYŁANIE DO FRONTU ===

    private void sendQueueUpdate() {
        messagingTemplate.convertAndSend("/topic/queue", queueRepository.findAll());
    }

    private void sendBreakUpdate() {
        messagingTemplate.convertAndSend("/topic/break", breakRepository.findAll());
    }
}
