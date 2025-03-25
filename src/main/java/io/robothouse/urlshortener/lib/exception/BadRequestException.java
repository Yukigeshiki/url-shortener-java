package io.robothouse.urlshortener.lib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public BadRequestException(String errMessage) {
        this.message = errMessage;
        this.status = HttpStatus.BAD_REQUEST;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
