package io.robothouse.urlshortener.model.response;


import io.robothouse.urlshortener.dto.BaseDTO;

import java.util.UUID;

public record Success(UUID requestId, BaseDTO payload, String status) implements BaseResponse {

    public Success(UUID requestId, BaseDTO payload) {
        this(requestId, payload, "succeeded");
    }
}
