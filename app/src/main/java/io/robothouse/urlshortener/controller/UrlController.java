package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.lib.util.KeyGenerator;
import io.robothouse.urlshortener.model.UrlAddRequestPayload;
import io.robothouse.urlshortener.model.UrlAddResponsePayload;
import io.robothouse.urlshortener.model.UrlDeleteResponsePayload;
import io.robothouse.urlshortener.model.UrlKeyPathVariable;
import io.robothouse.urlshortener.model.entity.UrlEntity;
import io.robothouse.urlshortener.service.UrlRedisService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
public class UrlController {

    private final UrlRedisService urlRedisService;

    @Value("${server.baseurl}")
    private String BASE_URL;

    public UrlController(UrlRedisService urlRedisService) {
        this.urlRedisService = urlRedisService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UrlAddResponsePayload urlAdd(@RequestBody UrlAddRequestPayload reqPayload) {
        log.info("Request payload: {}", reqPayload);

        String validLongUrl = reqPayload.parseLongUrl();
        String key = KeyGenerator.getKey(validLongUrl);

        Optional<UrlEntity> existingUrl = urlRedisService.get(key);
        while (existingUrl.isPresent()) {
            if (existingUrl.get().longUrl().equals(validLongUrl)) {
                // URL already exists in Redis
                UrlAddResponsePayload resPayload = new UrlAddResponsePayload(key, BASE_URL + key);
                log.info("Response payload: {}", resPayload);
                return resPayload;
            } else {
                // collision, generate new key and re-check
                key = KeyGenerator.getKey(validLongUrl + UUID.randomUUID());
                existingUrl = urlRedisService.get(key);
            }
        }

        String shortUrl = BASE_URL + key;
        UrlEntity url = new UrlEntity(key, validLongUrl, shortUrl);
        urlRedisService.add(url);
        log.info("Added to Redis: {}", url);
        UrlAddResponsePayload resPayload = new UrlAddResponsePayload(key, shortUrl);
        log.info("Response payload: {}", resPayload);

        return resPayload;
    }

    @GetMapping("/{key}")
    public SmartView urlRedirect(@PathVariable("key") UrlKeyPathVariable key) {
        String validKey = key.parseKey();
        UrlEntity url = urlRedisService.checkExistsAndGet(validKey);
        log.info("URL retrieved from redis: {}", url);
        return new RedirectView(url.longUrl());
    }

    @DeleteMapping("/{key}")
    public UrlDeleteResponsePayload urlDelete(@PathVariable("key") UrlKeyPathVariable key) {
        String validKey = key.parseKey();
        urlRedisService.checkExistsAndDelete(validKey);
        log.info("Deleted URL from redis with key: '{}'", validKey);
        UrlDeleteResponsePayload resPayload =
                new UrlDeleteResponsePayload(String.format("URL with key '%s' deleted successfully", validKey));
        log.info("Response payload: {}", resPayload);
        return resPayload;
    }
}