package io.robothouse.urlshortener.lib.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public NotFoundException(String errMessage) {
        this.message = errMessage;
        this.status = HttpStatus.NOT_FOUND;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
