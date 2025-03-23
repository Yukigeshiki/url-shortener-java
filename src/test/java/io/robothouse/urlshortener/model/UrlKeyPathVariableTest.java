package io.robothouse.urlshortener.model;

import io.robothouse.urlshortener.lib.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlKeyPathVariableTest {

    @Test
    void parseKeyWithValidKey() throws BadRequestException {
        UrlKeyPathVariable key = new UrlKeyPathVariable("Abc123Def456");
        assertEquals("Abc123Def456", key.parseKey());
    }

    @Test
    void parseKeyWithInvalidLength() {
        UrlKeyPathVariable key = new UrlKeyPathVariable("Abc123");
        BadRequestException exception = assertThrows(BadRequestException.class, key::parseKey);
        assertEquals("Validation errors: ['key' must be 12 characters in length]", exception.getMessage());
    }

    @Test
    void parseKeyWithNonAlphanumericCharacters() {
        UrlKeyPathVariable key = new UrlKeyPathVariable("Abc123!@#456");
        BadRequestException exception = assertThrows(BadRequestException.class, key::parseKey);
        assertEquals("Validation errors: ['key' can only have alphanumeric characters]", exception.getMessage());
    }

    @Test
    void parseKeyWithNullKey() {
        UrlKeyPathVariable key = new UrlKeyPathVariable("Abc13!@#456");
        BadRequestException exception = assertThrows(BadRequestException.class, key::parseKey);
        assertEquals("Validation errors: ['key' must be 12 characters in length, 'key' can only have alphanumeric characters]", exception.getMessage());
    }
}
