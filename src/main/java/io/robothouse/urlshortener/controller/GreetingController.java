package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.model.Greeting;
import io.robothouse.urlshortener.service.GreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/redis")
    public Greeting redisExample() {
        String key = "greeting";
        String message = "Hello from Redis with Repository Pattern!";
        greetingService.saveGreeting(key, message);
        return greetingService.getGreeting(key);
    }
}
