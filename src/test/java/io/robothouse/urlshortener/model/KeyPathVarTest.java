package io.robothouse.urlshortener.model;

import io.robothouse.urlshortener.lib.exception.HttpException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KeyPathVarTest {

    @Test
    void parseKeyWithValidKey() throws HttpException {
        KeyPathVar key = new KeyPathVar("Abc123Def456");
        assertEquals("Abc123Def456", key.parseKey());
    }

    @Test
    void parseKeyWithInvalidLength() {
        KeyPathVar key = new KeyPathVar("Abc123");
        HttpException exception = assertThrows(HttpException.class, key::parseKey);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, exception.getStatusCode());
        assertEquals("Validation errors: ['key' must be 12 characters in length]", exception.getMessage());
    }

    @Test
    void parseKeyWithNonAlphanumericCharacters() {
        KeyPathVar key = new KeyPathVar("Abc123!@#456");
        HttpException exception = assertThrows(HttpException.class, key::parseKey);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, exception.getStatusCode());
        assertEquals("Validation errors: ['key' can only have alphanumeric characters]", exception.getMessage());
    }

    @Test
    void parseKeyWithNullKey() {
        KeyPathVar key = new KeyPathVar("Abc13!@#456");
        HttpException exception = assertThrows(HttpException.class, key::parseKey);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, exception.getStatusCode());
        assertEquals("Validation errors: ['key' must be 12 characters in length, 'key' can only have alphanumeric characters]", exception.getMessage());
    }
}
