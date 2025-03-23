package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.model.entity.UrlEntity;
import io.robothouse.urlshortener.lib.exception.BadRequestException;
import io.robothouse.urlshortener.lib.exception.NotFoundException;
import io.robothouse.urlshortener.model.*;
import io.robothouse.urlshortener.service.UrlRedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.SmartView;
import org.springframework.web.servlet.view.RedirectView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UrlControllerTest {

    @Mock
    private UrlRedisService urlRedisService;

    @InjectMocks
    private UrlController urlController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void urlAddReturnsSuccessWhenUrlIsNew() {
        UrlAddRequestPayload reqPayload = new UrlAddRequestPayload("https://example.com");
        when(urlRedisService.get(anyString())).thenReturn(null);

        UrlAddResponsePayload result = urlController.urlAdd(reqPayload);

        assertInstanceOf(UrlAddResponsePayload.class, result);
        assertNotNull(result.key());
        verify(urlRedisService, times(1)).add(any(UrlEntity.class));
    }

    @Test
    void urlAddGeneratesNewKeyWhenCollisionOccurs() {
        UrlAddRequestPayload reqPayload = new UrlAddRequestPayload("https://example1.com");
        UrlEntity existingUrl = new UrlEntity("000000000000", "https://example2.com", "http://localhost:8080/000000000000");
        when(urlRedisService.get(anyString())).thenReturn(existingUrl).thenReturn(null);

        UrlAddResponsePayload result = urlController.urlAdd(reqPayload);

        assertInstanceOf(UrlAddResponsePayload.class, result);
        assertNotEquals("000000000000", result.key());
        verify(urlRedisService, times(1)).add(any(UrlEntity.class));
    }

    @Test
    void urlRedirectReturnsRedirectViewWhenKeyExists() throws BadRequestException {
        UrlKeyPathVariable key = new UrlKeyPathVariable("000000000000");
        UrlEntity url = new UrlEntity("000000000000", "https://example.com", "http://localhost:8080/000000000000");
        when(urlRedisService.checkExistsAndGet(anyString())).thenReturn(url);

        SmartView result = urlController.urlRedirect(key);

        assertInstanceOf(RedirectView.class, result);
        assertEquals("https://example.com", ((RedirectView) result).getUrl());
    }

    @Test
    void urlDeleteReturnsSuccessWhenKeyExists() throws BadRequestException {
        UrlKeyPathVariable key = new UrlKeyPathVariable("000000000000");
        doNothing().when(urlRedisService).checkExistsAndDelete(anyString());

        UrlDeleteResponsePayload result = urlController.urlDelete(key);

        assertInstanceOf(UrlDeleteResponsePayload.class, result);
        verify(urlRedisService, times(1)).checkExistsAndDelete(anyString());
    }

    @Test
    void urlRedirectReturnsNotFoundWhenKeyDoesNotExist() {
        UrlKeyPathVariable key = new UrlKeyPathVariable("000000000000");
        when(urlRedisService.checkExistsAndGet(anyString())).thenThrow(new NotFoundException("Url not found"));

        assertThrows(NotFoundException.class, () -> urlController.urlRedirect(key));
    }

    @Test
    void urlDeleteReturnsNotFoundWhenKeyDoesNotExist() {
        UrlKeyPathVariable key = new UrlKeyPathVariable("nonexistentKey");
        doThrow(new BadRequestException("Url not found")).when(urlRedisService).checkExistsAndDelete(anyString());

        assertThrows(BadRequestException.class, () -> urlController.urlDelete(key));
    }
}
