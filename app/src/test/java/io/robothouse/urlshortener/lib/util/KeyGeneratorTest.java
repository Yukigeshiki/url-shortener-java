package io.robothouse.urlshortener.lib.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {

    @Test
    void getKey_returnsNonNull() {
        assertNotNull(KeyGenerator.getKey("https://example.com"));
    }

    @Test
    void getKey_returnsStringOfCorrectLength() {
        assertEquals(12, KeyGenerator.getKey("https://example.com").length());
    }

    @Test
    void getKey_returnsDifferentKeysForDifferentInputs() {
        String url1 = "https://example.com";
        String url2 = "https://example.org";
        assertNotEquals(KeyGenerator.getKey(url1), KeyGenerator.getKey(url2));
    }

    @Test
    void getKey_handlesEmptyString() {
        assertNotNull(KeyGenerator.getKey(""));
    }

    @Test
    void getKey_handlesLongUrl() {
        String longUrl = "https://example.com/" + "a".repeat(1000);
        assertNotNull(KeyGenerator.getKey(longUrl));
    }
}
