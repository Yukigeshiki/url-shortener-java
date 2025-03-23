package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.model.entity.UrlEntity;
import io.robothouse.urlshortener.model.*;
import io.robothouse.urlshortener.service.UrlRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RestController
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);
    private final UrlRedisService urlRedisService;

    public UrlController(UrlRedisService urlRedisService) {
        this.urlRedisService = urlRedisService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UrlAddResponsePayload urlAdd(@RequestBody UrlAddRequestPayload reqPayload) {
        String baseUrl = "http://localhost:8080/";

        logger.info("Request payload: {}", reqPayload);
        String validLongUrl = reqPayload.parseLongUrl();
        String key = UrlEntity.createKey(validLongUrl);
        String shortUrl = baseUrl + key;

        UrlEntity existingUrl = urlRedisService.get(key);
        while (existingUrl != null) {
            if (existingUrl.longUrl().equals(validLongUrl)) {
                UrlAddResponsePayload resPayload = new UrlAddResponsePayload(key, shortUrl);
                logger.info("Response payload: {}", resPayload);
                return resPayload;
            } else {
                key = UrlEntity.createKey(validLongUrl + UUID.randomUUID());
                shortUrl = baseUrl + key;
            }
            existingUrl = urlRedisService.get(key);
        }

        UrlEntity url = new UrlEntity(key, validLongUrl, shortUrl);
        urlRedisService.add(url);
        logger.info("Added to Redis: {}", url);
        UrlAddResponsePayload resPayload = new UrlAddResponsePayload(key, shortUrl);
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
}
