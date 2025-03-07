package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.dto.UrlRequestDTO;
import io.robothouse.urlshortener.dto.UrlResponseDTO;
import io.robothouse.urlshortener.model.response.BaseResponse;
import io.robothouse.urlshortener.model.response.Fail;
import io.robothouse.urlshortener.model.response.Success;
import io.robothouse.urlshortener.model.Url;
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
    public BaseResponse urlAdd(@RequestBody UrlRequestDTO req, HttpServletResponse res) {
        UUID requestId = UUID.randomUUID();

        try {
            String key = Url.createKey(req.longUrl());
            String shortUrl = "http://localhost:8080/" + key;

            urlRedisService.add(new Url(key, req.longUrl(), shortUrl));

            res.setStatus(HttpServletResponse.SC_OK);
            return new Success(requestId, new UrlResponseDTO(shortUrl));
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
