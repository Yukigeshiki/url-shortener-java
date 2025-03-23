package io.robothouse.urlshortener.lib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    private final String errMessage;

    public NotFoundException(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getMessage() {
        return errMessage;
    }

    @Override
    public String toString() {
        return errMessage;
    }
}
