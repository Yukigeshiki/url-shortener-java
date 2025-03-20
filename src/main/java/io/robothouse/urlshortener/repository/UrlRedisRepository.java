package io.robothouse.urlshortener.repository;

import io.robothouse.urlshortener.model.url.Url;
import org.springframework.data.repository.CrudRepository;

public interface UrlRedisRepository extends CrudRepository<Url, String> {
}
