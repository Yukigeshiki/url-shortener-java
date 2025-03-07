package io.robothouse.urlshortener.model.response;


import io.robothouse.urlshortener.model.Model;

import java.util.UUID;

public record Success(UUID requestId, Model payload, String status) implements BaseResponse {

    public Success(UUID requestId, Model payload) {
        this(requestId, payload, "succeeded");
    }
}
