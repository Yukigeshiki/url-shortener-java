package io.robothouse.urlshortener.model.response;


import io.robothouse.urlshortener.model.ResPayload;

public record Success(String requestId, ResPayload payload, String status) implements BaseResponse {

    public Success(String requestId, ResPayload payload) {
        this(requestId, payload, "succeeded");
    }
}
