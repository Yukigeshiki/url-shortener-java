package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.lib.exception.HttpException;
import io.robothouse.urlshortener.model.response.BaseResponse;
import io.robothouse.urlshortener.model.response.Fail;
import io.robothouse.urlshortener.model.response.FailRedirect;
import io.robothouse.urlshortener.model.response.Success;
import io.robothouse.urlshortener.model.url.*;
import io.robothouse.urlshortener.service.UrlRedisService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
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
            while (existingUrl != null) {
                if (existingUrl.longUrl().equals(validLongUrl)) {
                    return setStatusAndRespond(requestId, res, new UrlAddResPayload(key, shortUrl));
                } else {
                    key = Url.createKey(validLongUrl + UUID.randomUUID());
                    shortUrl = baseUrl + key;
                }
                existingUrl = urlRedisService.get(key);
            }

            Url url = new Url(key, validLongUrl, shortUrl);
            urlRedisService.add(url);
            logger.info("Added to Redis: {}", url);
            return setStatusAndRespond(requestId, res, new UrlAddResPayload(key, shortUrl));
        } catch (Throwable err) {
            setErrStatus(err, res);
            return new Fail(requestId, err.getMessage());
        }
    }

    private static Success setStatusAndRespond(String requestId, HttpServletResponse res, UrlAddResPayload resPayload) {
        res.setStatus(HttpServletResponse.SC_OK);
        logger.info("Response payload: {}", resPayload);
        return new Success(requestId, resPayload);
    }

    @GetMapping("/{key}")
    public SmartView urlRedirect(@PathVariable("key") UrlKeyPathVar key, HttpServletResponse res) {
        String requestId = MDC.get("requestId");
        logger.info("Request url param: {}", key);

        try {
            String validKey = key.parseKey();
            Url url = urlRedisService.checkExistsAndGet(validKey);
            logger.info("Url retrieved from redis: {}", url);
            return new RedirectView(url.longUrl());
        } catch (Throwable err) {
            setErrStatus(err, res);
            return new FailRedirect(requestId, err.getMessage());
        }
    }

    @DeleteMapping("/{key}")
    public BaseResponse urlDelete(@PathVariable("key") UrlKeyPathVar key, HttpServletResponse res) {
        String requestId = MDC.get("requestId");
        logger.info("Request url param: {}", key);

        try {
            String validKey = key.parseKey();
            urlRedisService.checkExistsAndDelete(validKey);
            logger.info("Deleted Url from redis with key: '{}'", validKey);
            res.setStatus(HttpServletResponse.SC_OK);
            UrlDeleteResPayload resPayload =
                    new UrlDeleteResPayload(String.format("Url with key '%s' deleted successfully", validKey));
            logger.info("Response payload: {}", resPayload);
            return new Success(requestId, resPayload);
        } catch (Throwable err) {
            setErrStatus(err, res);
            return new Fail(requestId, err.getMessage());
        }
    }

    private static void setErrStatus(Throwable err, HttpServletResponse res) {
        if (err instanceof HttpException) {
            logger.error("Request failed with error: {}", err.toString());
            res.setStatus(((HttpException) err).getStatusCode());
        } else {
            logger.error("Stacktrace: {}", Arrays.toString(err.getStackTrace()));
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
