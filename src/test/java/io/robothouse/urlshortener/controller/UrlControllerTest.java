package io.robothouse.urlshortener.controller;

import io.robothouse.urlshortener.lib.exception.HttpException;
import io.robothouse.urlshortener.model.*;
import io.robothouse.urlshortener.model.response.BaseResponse;
import io.robothouse.urlshortener.model.response.Success;
import io.robothouse.urlshortener.service.UrlRedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
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

    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        response = new MockHttpServletResponse();
    }

    @Test
    void testUrlAdd() {
        UrlAddReqPayload reqPayload = new UrlAddReqPayload("https://example.com");

        when(urlRedisService.get(anyString())).thenReturn(null);
        doNothing().when(urlRedisService).add(any(Url.class));

        BaseResponse result = urlController.urlAdd(reqPayload, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertInstanceOf(Success.class, result);
        verify(urlRedisService, times(1)).add(any(Url.class));
    }

    @Test
    void testUrlRedirect() throws HttpException {
        KeyPathVar key = new KeyPathVar("000000000000");
        Url url = new Url("000000000000", "https://example.com", "http://localhost:8080/000000000000");

        when(urlRedisService.checkExistsAndGet(anyString())).thenReturn(url);

        SmartView result = urlController.urlRedirect(key, response);

        assertInstanceOf(RedirectView.class, result);
        assertEquals("https://example.com", ((RedirectView) result).getUrl());
    }

    @Test
    void testUrlDelete() throws HttpException {
        KeyPathVar key = new KeyPathVar("000000000000");

        doNothing().when(urlRedisService).checkExistsAndDelete(anyString());

        BaseResponse result = urlController.urlDelete(key, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertInstanceOf(Success.class, result);
        verify(urlRedisService, times(1)).checkExistsAndDelete(anyString());
    }
}
