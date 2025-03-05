package io.robothouse.urlshortener.repository;

import io.robothouse.urlshortener.model.Greeting;
import org.springframework.data.repository.CrudRepository;

public interface GreetingRepository extends CrudRepository<Greeting, String> {
}
