package io.robothouse.urlshortener.model.url;


import io.robothouse.urlshortener.model.ResPayload;

public record UrlAddResPayload(String key, String shortUrl) implements ResPayload {
}
