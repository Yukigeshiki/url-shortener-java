package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.lib.exception.HttpException;
import io.robothouse.urlshortener.model.*;
import io.robothouse.urlshortener.model.response.BaseResponse;
import io.robothouse.urlshortener.model.response.Fail;
import io.robothouse.urlshortener.model.response.FailRedirect;
import io.robothouse.urlshortener.model.response.Success;
import io.robothouse.urlshortener.service.UrlRedisService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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
    public BaseResponse urlAdd(@RequestBody UrlAddReqPayload reqPayload, HttpServletResponse res) {
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
                    UrlAddResPayload resPayload = new UrlAddResPayload(key, shortUrl);
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
            UrlAddResPayload resPayload = new UrlAddResPayload(key, shortUrl);
            logger.info("Response payload: {}", resPayload);
            return new Success(requestId, resPayload);
        } catch (Throwable err) {
            manageExceptionType(err, res);
            logger.error("Request failed with error: {}", err.toString());
            return new Fail(requestId, err.getMessage());
        }
    }

    @GetMapping("/{key}")
    public SmartView urlRedirect(@PathVariable("key") KeyPathVar key, HttpServletResponse res) {
        String requestId = MDC.get("requestId");
        logger.info("Request url param: {}", key);

        try {
            String validKey = key.parseKey();
            Url url = urlRedisService.checkExistsAndGet(validKey);
            logger.info("Url retrieved from redis: {}", url);
            return new RedirectView(url.longUrl());
        } catch (Throwable err) {
            manageExceptionType(err, res);
            logger.error("Request failed with error: {}", err.toString());
            return new FailRedirect(requestId, err.getMessage());
        }
    }

    @DeleteMapping("/{key}")
    public BaseResponse urlDelete(@PathVariable("key") KeyPathVar key, HttpServletResponse res) {
        String requestId = MDC.get("requestId");
        logger.info("Request url param: {}", key);

        try {
            String validKey = key.parseKey();
            urlRedisService.checkExistsAndDelete(validKey);
            logger.info("Deleted Url from redis with key: '{}'", validKey);

            res.setStatus(HttpServletResponse.SC_OK);
            UrlDeleteResPayload resPayload = new UrlDeleteResPayload(String.format("Url with key '%s' deleted successfully", validKey));
            logger.info("Response payload: {}", resPayload);
            return new Success(requestId, resPayload);
        } catch (Throwable err) {
            manageExceptionType(err, res);
            logger.error("Request failed with error: {}", err.toString());
            return new Fail(requestId, err.getMessage());
        }
    }
    
    private static void manageExceptionType(Throwable err, HttpServletResponse res) {
        if (err instanceof HttpException) {
            res.setStatus(((HttpException) err).getStatusCode());
        } else {
            err.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
