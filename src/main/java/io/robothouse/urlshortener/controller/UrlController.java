package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.model.UrlAddRequestPayload;
import io.robothouse.urlshortener.model.UrlAddResponsePayload;
import io.robothouse.urlshortener.model.UrlDeleteResponsePayload;
import io.robothouse.urlshortener.model.UrlKeyPathVariable;
import io.robothouse.urlshortener.model.entity.UrlEntity;
import io.robothouse.urlshortener.service.UrlRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;
import java.util.UUID;

@RestController
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);
    private static final String BASE_URL = "http://localhost:8080/";
    private final UrlRedisService urlRedisService;

    public UrlController(UrlRedisService urlRedisService) {
        this.urlRedisService = urlRedisService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UrlAddResponsePayload urlAdd(@RequestBody UrlAddRequestPayload reqPayload) {
        logger.info("Request payload: {}", reqPayload);
        String validLongUrl = reqPayload.parseLongUrl();

        String uniqueKey = createUniqueKey(validLongUrl);
        String shortUrl = BASE_URL + uniqueKey;

        UrlEntity url = new UrlEntity(uniqueKey, validLongUrl, shortUrl);
        urlRedisService.add(url);
        logger.info("Added to Redis: {}", url);
        UrlAddResponsePayload resPayload = new UrlAddResponsePayload(uniqueKey, shortUrl);
        logger.info("Response payload: {}", resPayload);

        return resPayload;
    }

    @GetMapping("/{key}")
    public SmartView urlRedirect(@PathVariable("key") UrlKeyPathVariable key) {
        String validKey = key.parseKey();
        UrlEntity url = urlRedisService.checkExistsAndGet(validKey);
        logger.info("Url retrieved from redis: {}", url);
        return new RedirectView(url.longUrl());
    }

    @DeleteMapping("/{key}")
    public UrlDeleteResponsePayload urlDelete(@PathVariable("key") UrlKeyPathVariable key) {
        String validKey = key.parseKey();
        urlRedisService.checkExistsAndDelete(validKey);
        logger.info("Deleted Url from redis with key: '{}'", validKey);
        UrlDeleteResponsePayload resPayload =
                new UrlDeleteResponsePayload(String.format("Url with key '%s' deleted successfully", validKey));
        logger.info("Response payload: {}", resPayload);
        return resPayload;
    }

    private String createUniqueKey(String longUrl) {
        String key = UrlEntity.createKey(longUrl);
        Optional<UrlEntity> existingUrl = urlRedisService.get(key);
        while (existingUrl.isPresent()) {
            if (existingUrl.get().longUrl().equals(longUrl)) {
                return key;
            } else {
                key = UrlEntity.createKey(longUrl + UUID.randomUUID());
            }
            existingUrl = urlRedisService.get(key);
        }
        return key;
    }
}