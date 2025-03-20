package io.robothouse.urlshortener.service;


import io.robothouse.urlshortener.lib.exception.HttpException;
import io.robothouse.urlshortener.model.url.Url;
import io.robothouse.urlshortener.repository.UrlRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class UrlRedisService {

    private final UrlRepository urlRepository;
    private final String NOT_FOUND_MSG = "Url with key '%s' does not exist";

    public UrlRedisService(UrlRepository greetingRepository) {
        this.urlRepository = greetingRepository;
    }

    public void add(Url url) {
        urlRepository.save(url);
    }

    public Url get(String key) {
        return urlRepository.findById(key).orElse(null);
    }

    public Url checkExistsAndGet(String key) throws HttpException {
        if (urlRepository.existsById(key)) {
            return urlRepository.findById(key).orElse(null);
        } else {
            throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format(NOT_FOUND_MSG, key));
        }
    }

    public void checkExistsAndDelete(String key) throws HttpException {
        if (urlRepository.existsById(key)) {
            urlRepository.deleteById(key);
        } else {
            throw new HttpException(HttpServletResponse.SC_NOT_FOUND, String.format(NOT_FOUND_MSG, key));
        }
    }
}
