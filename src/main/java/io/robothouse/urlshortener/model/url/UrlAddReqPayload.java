package io.robothouse.urlshortener.model.url;

import io.robothouse.urlshortener.lib.exception.HttpException;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public record UrlAddReqPayload(String longUrl) {

    public String parseLongUrl() throws HttpException {
        ArrayList<String> validationErrors = new ArrayList<>(List.of());

        // dependent checks (if else)
        if (longUrl == null || longUrl.isBlank()) {
            validationErrors.add("'longUrl' cannot be null or empty");
        } else if (!isValidUrl()) {
            validationErrors.add("'longUrl' is not a valid URL");
        }

        if (!validationErrors.isEmpty()) {
            String errString = String.format("Validation errors: %s", validationErrors);
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, errString);
        } else {
            return longUrl;
        }
    }

    private boolean isValidUrl() {
        try {
            URI uri = new URI(longUrl);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
