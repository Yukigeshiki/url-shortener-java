package io.robothouse.urlshortener.model;

import io.robothouse.urlshortener.lib.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;

public record UrlKeyPathVariable(String key) {

    public String parseKey() throws BadRequestException {
        List<String> validationErrors = new ArrayList<>();

        // independent checks (if per check)
        if (key.length() != 12) {
            validationErrors.add("'key' must be 12 characters in length");
        }
        if (!key.matches("[A-Za-z0-9]+")) {
            validationErrors.add("'key' can only have alphanumeric characters");
        }

        if (!validationErrors.isEmpty()) {
            String errString = String.format("Validation errors: %s", validationErrors);
            throw new BadRequestException(errString);
        }

        return key;
    }
}
