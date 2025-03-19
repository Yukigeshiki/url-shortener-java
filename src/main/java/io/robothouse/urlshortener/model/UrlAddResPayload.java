package io.robothouse.urlshortener.model;


public record UrlAddResPayload(String key, String shortUrl) implements Model {
}
