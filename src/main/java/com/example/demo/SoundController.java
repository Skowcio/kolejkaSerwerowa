package com.example.demo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SoundController {

    @MessageMapping("/ring")
    @SendTo("/topic/sound")
    public String ringBell() {
        return "play";
    }
}
