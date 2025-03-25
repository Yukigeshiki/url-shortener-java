package io.robothouse.urlshortener.service;


import io.robothouse.urlshortener.lib.exception.NotFoundException;
import io.robothouse.urlshortener.model.entity.UrlEntity;
import io.robothouse.urlshortener.repository.UrlRedisRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlRedisService {

    private final UrlRedisRepository urlRedisRepository;
    private static final String NOT_FOUND_MSG = "URL with key '%s' does not exist";

    public UrlRedisService(UrlRedisRepository greetingRepository) {
        this.urlRedisRepository = greetingRepository;
    }

    public void add(UrlEntity url) {
        urlRedisRepository.save(url);
    }

    public Optional<UrlEntity> get(String key) {
        return urlRedisRepository.findById(key);
    }

    public UrlEntity checkExistsAndGet(String key) throws NotFoundException {
        return urlRedisRepository.findById(key)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_MSG, key)));
    }

    public void checkExistsAndDelete(String key) throws NotFoundException {
        if (urlRedisRepository.existsById(key)) {
            urlRedisRepository.deleteById(key);
        } else {
            throw new NotFoundException(String.format(NOT_FOUND_MSG, key));
        }
    }
}
