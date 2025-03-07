package io.robothouse.urlshortener.model;

import io.robothouse.urlshortener.lib.exception.HttpException;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URI;
import java.net.URISyntaxException;

public record LongUrl(String longUrl) {

    public String parse() throws HttpException {
        if (longUrl == null || longUrl.isBlank()) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "\"longUrl\" cannot be null or empty");
        }
        if (!isValidUrl()) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "\"longUrl\" is not a valid URL");
        }
        return this.longUrl;
    }

    private boolean isValidUrl() {
        try {
            URI uri = new URI(longUrl);
            // Check if the URI has a valid scheme and host
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
