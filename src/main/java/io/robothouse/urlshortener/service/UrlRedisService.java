package io.robothouse.urlshortener.service;


import io.robothouse.urlshortener.lib.exception.HttpException;
import io.robothouse.urlshortener.model.Url;
import io.robothouse.urlshortener.repository.UrlRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class UrlRedisService {

    private final UrlRepository urlRepository;

    public UrlRedisService(UrlRepository greetingRepository) {
        this.urlRepository = greetingRepository;
    }

    public void add(Url url) {
        urlRepository.save(url);
    }

    public Url get(String key) {
        return urlRepository.findById(key).orElse(null);
    }

    public void delete(String key) throws HttpException {
        if (urlRepository.existsById(key)) {
            urlRepository.deleteById(key);
        } else {
            throw new HttpException(HttpServletResponse.SC_NOT_FOUND, "Url does not exist");
        }
    }
}
