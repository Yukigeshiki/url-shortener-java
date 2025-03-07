package io.robothouse.urlshortener.lib.exception;

public class HttpException extends Throwable {
    private final int statusCode;
    private final String message;

    public HttpException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
