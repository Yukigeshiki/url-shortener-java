package io.robothouse.urlshortener.model.response;

public record Fail(String requestId, String err, String status) implements BaseResponse {

    public Fail(String requestId, String err) {
        this(requestId, err, "failed");
    }
}
