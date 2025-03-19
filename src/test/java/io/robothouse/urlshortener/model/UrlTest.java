package io.robothouse.urlshortener.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlTest {

    @Test
    void createKeyWithValidUrl() {
        String key = Url.createKey("https://example.com");
        assertEquals("100680ad546c", key);
    }
}
