package io.robothouse.urlshortener.model.url;

import io.robothouse.urlshortener.model.ResPayload;

public record UrlDeleteResPayload(String msg) implements ResPayload {
}
