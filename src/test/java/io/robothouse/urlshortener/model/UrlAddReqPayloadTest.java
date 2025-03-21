package io.robothouse.urlshortener.model;

import io.robothouse.urlshortener.lib.exception.HttpException;
import io.robothouse.urlshortener.model.url.UrlAddReqPayload;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlAddReqPayloadTest {

    @Test
    void parseLongUrlWithValidUrl() throws HttpException {
        UrlAddReqPayload urlAddReq = new UrlAddReqPayload("https://example.com");
        assertEquals("https://example.com", urlAddReq.parseLongUrl());
    }

    @Test
    void parseLongUrlWithNullUrl() {
        UrlAddReqPayload urlAddReq = new UrlAddReqPayload(null);
        HttpException exception = assertThrows(HttpException.class, urlAddReq::parseLongUrl);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, exception.getStatusCode());
        assertEquals("Validation errors: ['longUrl' cannot be null or empty]", exception.getMessage());
    }

    @Test
    void parseLongUrlWithEmptyUrl() {
        UrlAddReqPayload urlAddReq = new UrlAddReqPayload("");
        HttpException exception = assertThrows(HttpException.class, urlAddReq::parseLongUrl);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, exception.getStatusCode());
        assertEquals("Validation errors: ['longUrl' cannot be null or empty]", exception.getMessage());
    }

    @Test
    void parseLongUrlWithInvalidUrl() {
        UrlAddReqPayload urlAddReq = new UrlAddReqPayload("invalid-url");
        HttpException exception = assertThrows(HttpException.class, urlAddReq::parseLongUrl);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, exception.getStatusCode());
        assertEquals("Validation errors: ['longUrl' is not a valid URL]", exception.getMessage());
    }

    @Test
    void parseLongUrlWithUrlWithoutScheme() {
        UrlAddReqPayload urlAddReq = new UrlAddReqPayload("example.com");
        HttpException exception = assertThrows(HttpException.class, urlAddReq::parseLongUrl);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, exception.getStatusCode());
        assertEquals("Validation errors: ['longUrl' is not a valid URL]", exception.getMessage());
    }

    @Test
    void parseLongUrlWithUrlWithoutHost() {
        UrlAddReqPayload urlAddReq = new UrlAddReqPayload("https://");
        HttpException exception = assertThrows(HttpException.class, urlAddReq::parseLongUrl);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, exception.getStatusCode());
        assertEquals("Validation errors: ['longUrl' is not a valid URL]", exception.getMessage());
    }
}
