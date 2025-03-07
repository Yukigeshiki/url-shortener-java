package io.robothouse.urlshortener.model.response;

import io.robothouse.urlshortener.lib.exception.HttpException;
import jakarta.servlet.http.HttpServletResponse;

public record Fail(String requestId, String err, String status) implements BaseResponse {

    public Fail(String requestId, String err) {
        this(requestId, err, "failed");
    }

    public Fail andHandleException(HttpServletResponse response, Throwable err) {
        if (err instanceof HttpException) {
            response.setStatus(((HttpException) err).getStatusCode());
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return this;
    }
}
