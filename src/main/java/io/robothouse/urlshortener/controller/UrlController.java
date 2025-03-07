package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.model.Url;
import io.robothouse.urlshortener.model.UrlRequest;
import io.robothouse.urlshortener.model.UrlResponse;
import io.robothouse.urlshortener.model.response.BaseResponse;
import io.robothouse.urlshortener.model.response.Fail;
import io.robothouse.urlshortener.model.response.Success;
import io.robothouse.urlshortener.service.UrlRedisService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UrlController {

    private final UrlRedisService urlRedisService;

    public UrlController(UrlRedisService urlRedisService) {
        this.urlRedisService = urlRedisService;
    }

    @PostMapping("/")
    public BaseResponse urlAdd(@RequestBody UrlRequest req, HttpServletResponse res) {
        UUID requestId = UUID.randomUUID();
        String baseUrl = "http://localhost:8080/";

        try {
            String validLongUrl = req.parseLongUrl();
            String key = Url.createKey(validLongUrl);
            String shortUrl = baseUrl + key;

            urlRedisService.add(new Url(key, validLongUrl, shortUrl));

            res.setStatus(HttpServletResponse.SC_OK);
            return new Success(requestId, new UrlResponse(shortUrl));
        } catch (Throwable err) {
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
