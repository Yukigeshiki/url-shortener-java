package io.robothouse.urlshortener.model;

import io.robothouse.urlshortener.lib.exception.HttpException;
import jakarta.servlet.http.HttpServletResponse;

public record Key(String key) {

    public String parseKey() throws HttpException {
        if (key.length() != 12) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "'key' must be 12 characters in length");
        } else if (!key.matches("[A-Za-z0-9]+")) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "'key' can only have alphanumeric characters");
        } else {
            return key;
        }
    }
}

