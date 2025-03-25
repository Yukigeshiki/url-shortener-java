package io.robothouse.urlshortener.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Url")
public record UrlEntity(@Id String key, String longUrl, String shortUrl) {
}
