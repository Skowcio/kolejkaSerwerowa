package com.example.demo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SoundController {

    @MessageMapping("/ring")
    @SendTo("/topic/sound")
    public String ringBell1() {
        return "wezel1";
    }

    @MessageMapping("/ring2")
    @SendTo("/topic/sound")
    public String ringBell2() {
        return "wezel2";
    }

    @MessageMapping("/ring3")
    @SendTo("/topic/sound")
    public String ringBell3() {
        return "TEST";
    }
}