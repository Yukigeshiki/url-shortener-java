package io.robothouse.urlshortener.service;


import io.robothouse.urlshortener.model.Url;
import io.robothouse.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

@Service
public class UrlRedisService {

    private final UrlRepository urlRepository;

    public UrlRedisService(UrlRepository greetingRepository) {
        this.urlRepository = greetingRepository;
    }

    public void add(Url url)  {
        urlRepository.save(url);
    }

    public Url get(String key) {
        return urlRepository.findById(key).orElse(null);
    }

    public Url delete(String key) {
        return urlRepository.findById(key).orElse(null);
    }
}
