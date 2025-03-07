package io.robothouse.urlshortener.model.response;


import io.robothouse.urlshortener.model.Model;

public record Success(String requestId, Model payload, String status) implements BaseResponse {

    public Success(String requestId, Model payload) {
        this(requestId, payload, "succeeded");
    }
}
