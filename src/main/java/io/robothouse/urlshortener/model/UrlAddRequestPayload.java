package io.robothouse.urlshortener.model;

import io.robothouse.urlshortener.lib.exception.BadRequestException;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public record UrlAddRequestPayload(String longUrl) {

    public String parseLongUrl() throws BadRequestException {
        List<String> validationErrors = new ArrayList<>();

        // dependent checks (if else)
        if (StringUtils.isEmpty(longUrl)) {
            validationErrors.add("'longUrl' cannot be null or empty");
        } else if (!isValidUrl()) {
            validationErrors.add("'longUrl' is not a valid URL");
        }

        if (!validationErrors.isEmpty()) {
            String errString = String.format("Validation errors: %s", validationErrors);
            throw new BadRequestException(errString);
        }

        return longUrl;
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
