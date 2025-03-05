package io.robothouse.urlshortener.model;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("Greeting")
public record Greeting(String id, String message) { }
