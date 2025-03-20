package io.robothouse.urlshortener.service;


import io.robothouse.urlshortener.lib.exception.HttpException;
import io.robothouse.urlshortener.model.url.Url;
import io.robothouse.urlshortener.repository.UrlRedisRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class UrlRedisService {

    private final UrlRedisRepository urlRedisRepository;
    private final String NOT_FOUND_MSG = "Url with key '%s' does not exist";

    public UrlRedisService(UrlRedisRepository greetingRepository) {
        this.urlRedisRepository = greetingRepository;
    }

    public void add(Url url) {
        urlRedisRepository.save(url);
    }

    public Url get(String key) {
        return urlRedisRepository.findById(key).orElse(null);
    }

    public Url checkExistsAndGet(String key) throws HttpException {
        if (urlRedisRepository.existsById(key)) {
            return urlRedisRepository.findById(key).orElse(null);
        } else {
            throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format(NOT_FOUND_MSG, key));
        }
    }

    public void checkExistsAndDelete(String key) throws HttpException {
        if (urlRedisRepository.existsById(key)) {
            urlRedisRepository.deleteById(key);
        } else {
            throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format(NOT_FOUND_MSG, key));
        }
    }
}
