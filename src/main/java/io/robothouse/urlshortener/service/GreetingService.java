package io.robothouse.urlshortener.service;


import io.robothouse.urlshortener.model.Greeting;
import io.robothouse.urlshortener.repository.GreetingRepository;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    public void saveGreeting(String id, String message) {
        Greeting greeting = new Greeting(id, message);
        greetingRepository.save(greeting);
    }

    public Greeting getGreeting(String id) {
        return greetingRepository.findById(id).orElse(null);
    }
}
