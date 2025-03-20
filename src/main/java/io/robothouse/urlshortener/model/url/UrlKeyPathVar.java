package io.robothouse.urlshortener.model.url;

import io.robothouse.urlshortener.lib.exception.HttpException;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

public record UrlKeyPathVar(String key) {

    public String parseKey() throws HttpException {
        ArrayList<String> validationErrors = new ArrayList<>(List.of());

        // independent checks (if per check)
        if (key.length() != 12) {
            validationErrors.add("'key' must be 12 characters in length");
        }
        if (!key.matches("[A-Za-z0-9]+")) {
            validationErrors.add("'key' can only have alphanumeric characters");
        }

        if (!validationErrors.isEmpty()) {
            String errString = String.format("Validation errors: %s", validationErrors);
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, errString);
        } else {
            return key;
        }
    }
}
