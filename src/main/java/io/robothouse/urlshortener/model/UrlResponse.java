package io.robothouse.urlshortener.model;


public record UrlResponse(String key, String shortUrl) implements Model {
}
