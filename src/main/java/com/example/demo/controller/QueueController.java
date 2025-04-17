package com.example.demo.controller;

import com.example.demo.model.BreakEntry;
import com.example.demo.model.QueueEntry;
import com.example.demo.repository.BreakRepository;
import com.example.demo.repository.QueueRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

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

//    @GetMapping("/add")
//    public String addToQueue(@RequestParam String name) {
//        // Pobieramy całą kolejkę posortowaną rosnąco po pozycji
//        List<QueueEntry> allEntries = queueRepository.findAllByOrderByPositionAsc();
//
//        // Nowa pozycja to ostatnia + 1, albo 0 jeśli to pierwszy wpis
//        int newPosition = allEntries.isEmpty() ? 0 : allEntries.get(allEntries.size() - 1).getPosition() + 1;
//
//        // Tworzymy nowy wpis z nazwą i pozycją
//        QueueEntry newEntry = new QueueEntry();
//        newEntry.setName(name);
//        newEntry.setPosition(newPosition);
//
//        // Zapisujemy do bazy
//        queueRepository.save(newEntry);
//        sendQueueUpdate();
//
//        return name + " added to queue";
//    }

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
//    @GetMapping("/queue/moveUp")
//    public ResponseEntity<String> moveUp(@RequestParam String name) {
//        Optional<QueueEntry> entryOpt = queueRepository.findByName(name);
//        if (entryOpt.isEmpty()) {
//            return ResponseEntity.badRequest().body("Nie znaleziono");
//        }
//
//        QueueEntry entry = entryOpt.get();
//        int currentPosition = entry.getPosition();
//
//        if (currentPosition == 0) {
//            return ResponseEntity.ok("Już jest na górze");
//        }
//
//        Optional<QueueEntry> aboveOpt = queueRepository.findByPosition(currentPosition - 1);
//        if (aboveOpt.isPresent()) {
//            QueueEntry above = aboveOpt.get();
//
//            // zamiana miejsc
//            above.setPosition(currentPosition);
//            entry.setPosition(currentPosition - 1);
//
//            queueRepository.save(above);
//            queueRepository.save(entry);
//
//            return ResponseEntity.ok("Przesunięto w górę");
//        } else {
//            return ResponseEntity.ok("Brak osoby wyżej");
//        }
//    }


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
    public String removeFromBreak() {
        List<BreakEntry> all = breakRepository.findAll();
        if (!all.isEmpty()) {
            BreakEntry first = all.get(0);
            breakRepository.delete(first);
            sendBreakUpdate();
            return first.getName() + " removed from front of queue";
        } else {
            return "Queue is empty";
        }
    }
    @GetMapping("/break/removeByName")
    public String removeFromBreakByName(@RequestParam String name) {
        breakRepository.deleteByName(name);
        sendBreakUpdate();
        return name + " removed from break list";
    }

    @GetMapping("/break/all")
    public List<BreakEntry> getAllBreak() {
        return breakRepository.findAll();
    }

    // === DODATKOWE MAPOWANIA ===

    @GetMapping("")
    public List<QueueEntry> getDefaultQueue() {
        return queueRepository.findAll();
    }

    @GetMapping("/break")
    public List<BreakEntry> getDefaultBreakQueue() {
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
