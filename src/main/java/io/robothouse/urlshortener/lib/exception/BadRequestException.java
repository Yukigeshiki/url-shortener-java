package io.robothouse.urlshortener.lib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private final String errMessage;

    public BadRequestException(String errMessage) {
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
