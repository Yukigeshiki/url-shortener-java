package io.robothouse.urlshortener.model;

import io.robothouse.urlshortener.lib.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlAddReqPayloadTest {

    @Test
    void parseLongUrlWithValidUrl() throws BadRequestException {
        UrlAddRequestPayload urlAddReq = new UrlAddRequestPayload("https://example.com");
        assertEquals("https://example.com", urlAddReq.parseLongUrl());
    }

    @Test
    void parseLongUrlWithNullUrl() {
        UrlAddRequestPayload urlAddReq = new UrlAddRequestPayload(null);
        BadRequestException exception = assertThrows(BadRequestException.class, urlAddReq::parseLongUrl);
        assertEquals("Validation errors: ['longUrl' cannot be null or empty]", exception.getMessage());
    }

    @Test
    void parseLongUrlWithEmptyUrl() {
        UrlAddRequestPayload urlAddReq = new UrlAddRequestPayload("");
        BadRequestException exception = assertThrows(BadRequestException.class, urlAddReq::parseLongUrl);
        assertEquals("Validation errors: ['longUrl' cannot be null or empty]", exception.getMessage());
    }

    @Test
    void parseLongUrlWithInvalidUrl() {
        UrlAddRequestPayload urlAddReq = new UrlAddRequestPayload("invalid-url");
        BadRequestException exception = assertThrows(BadRequestException.class, urlAddReq::parseLongUrl);
        assertEquals("Validation errors: ['longUrl' is not a valid URL]", exception.getMessage());
    }

    @Test
    void parseLongUrlWithUrlWithoutScheme() {
        UrlAddRequestPayload urlAddReq = new UrlAddRequestPayload("example.com");
        BadRequestException exception = assertThrows(BadRequestException.class, urlAddReq::parseLongUrl);
        assertEquals("Validation errors: ['longUrl' is not a valid URL]", exception.getMessage());
    }

    @Test
    void parseLongUrlWithUrlWithoutHost() {
        UrlAddRequestPayload urlAddReq = new UrlAddRequestPayload("https://");
        BadRequestException exception = assertThrows(BadRequestException.class, urlAddReq::parseLongUrl);
        assertEquals("Validation errors: ['longUrl' is not a valid URL]", exception.getMessage());
    }
}
