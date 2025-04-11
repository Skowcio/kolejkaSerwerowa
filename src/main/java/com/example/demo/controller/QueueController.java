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

    public QueueController(QueueRepository queueRepository,
                           BreakRepository breakRepository,
                           SimpMessagingTemplate messagingTemplate) {
        this.queueRepository = queueRepository;
        this.breakRepository = breakRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/add")
    public String addToQueue(@RequestParam String name) {
        queueRepository.save(new QueueEntry(name));
        updateQueue();
        return name + " added to queue";
    }

    @GetMapping("/add-special")
    public String addAsSpecial(@RequestParam String name) {
        List<QueueEntry> current = queueRepository.findAll();
        queueRepository.deleteAll();
        QueueEntry special = new QueueEntry(name);
        queueRepository.save(special);
        queueRepository.saveAll(current);
        updateQueue();
        return name + " added as special to front of queue";
    }

    @GetMapping("/remove")
    public String removeFromQueue(@RequestParam String name) {
        queueRepository.deleteByName(name);
        updateQueue();
        return name + " removed from queue";
    }

    @GetMapping("/removeFirst")
    public String removeFirst() {
        List<QueueEntry> all = queueRepository.findAll();
        if (!all.isEmpty()) {
            QueueEntry first = all.get(0);
            queueRepository.delete(first);
            updateQueue();
            return first.getName() + " removed from front of queue";
        } else {
            return "Queue is empty";
        }
    }

    @GetMapping("/break/add")
    public String addToBreak(@RequestParam String name) {
        if (breakRepository.findByName(name).isEmpty()) {
            breakRepository.save(new BreakEntry(name));
            updateBreakList();
            return name + " added to break list";
        } else {
            return name + " is already on break";
        }
    }

    @GetMapping("/break/remove")
    public String removeFromBreak(@RequestParam String name) {
        breakRepository.deleteByName(name);
        updateBreakList();
        return name + " removed from break list";
    }

    @GetMapping
    public List<QueueEntry> getQueue() {
        return queueRepository.findAll();
    }

    @GetMapping("/break")
    public List<BreakEntry> getBreakList() {
        return breakRepository.findAll();
    }

    @GetMapping("/all")
    public List<String> getAllQueueNames() {
        return queueRepository.findAll().stream().map(QueueEntry::getName).toList();
    }

    @GetMapping("/break/all")
    public List<String> getAllBreakNames() {
        return breakRepository.findAll().stream().map(BreakEntry::getName).toList();
    }

    private void updateQueue() {
        List<String> names = queueRepository.findAll().stream().map(QueueEntry::getName).toList();
        messagingTemplate.convertAndSend("/topic/queue", names);
    }

    private void updateBreakList() {
        List<String> names = breakRepository.findAll().stream().map(BreakEntry::getName).toList();
        messagingTemplate.convertAndSend("/topic/break", names);
    }
}
