package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.model.Url;
import io.robothouse.urlshortener.model.UrlRequest;
import io.robothouse.urlshortener.model.UrlResponse;
import io.robothouse.urlshortener.model.response.BaseResponse;
import io.robothouse.urlshortener.model.response.Fail;
import io.robothouse.urlshortener.model.response.Success;
import io.robothouse.urlshortener.service.UrlRedisService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);
    private final UrlRedisService urlRedisService;

    public UrlController(UrlRedisService urlRedisService) {
        this.urlRedisService = urlRedisService;
    }

    @PostMapping("/")
    public BaseResponse urlAdd(@RequestBody UrlRequest reqPayload, HttpServletResponse res) {
        String requestId = MDC.get("requestId");
        String baseUrl = "http://localhost:8080/";

        logger.info("Request payload: {}", reqPayload);

        try {
            String validLongUrl = reqPayload.parseLongUrl();
            String key = Url.createKey(validLongUrl);
            String shortUrl = baseUrl + key;

            Url existingUrl = urlRedisService.get(key);
            if (existingUrl != null) {
                if (existingUrl.longUrl().equals(validLongUrl)) {
                    res.setStatus(HttpServletResponse.SC_OK);
                    UrlResponse resPayload = new UrlResponse(key, shortUrl);
                    logger.info("Response payload: {}", resPayload);
                    return new Success(requestId, resPayload);
                } else {
                    key = Url.createKey(validLongUrl + UUID.randomUUID());
                    shortUrl = baseUrl + key;
                }
            }

            Url url = new Url(key, validLongUrl, shortUrl);
            urlRedisService.add(url);
            logger.info("Added to Redis: {}", url);

            res.setStatus(HttpServletResponse.SC_OK);
            UrlResponse resPayload = new UrlResponse(key, shortUrl);
            logger.info("Response payload: {}", resPayload);
            return new Success(requestId, resPayload);
        } catch (Throwable err) {
            logger.error("Request failed with error: {}", err.getMessage());
            return new Fail(requestId, err.getMessage()).andHandleException(res, err);
        }
    }

    @GetMapping("/{key}")
    public BaseResponse urlRedirect(@PathVariable("key") String key) {
        return null;
    }

    @DeleteMapping("/{key}")
    public BaseResponse urlDelete(@PathVariable("key") String key) {
        return null;
    }
}
