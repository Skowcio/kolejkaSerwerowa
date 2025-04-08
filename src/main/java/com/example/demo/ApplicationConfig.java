package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig implements CommandLineRunner {

    @Value("${PORT}")
    private String port;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("PORT: " + port);
    }
}