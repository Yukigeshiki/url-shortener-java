package io.robothouse.urlshortener.repository;

import io.robothouse.urlshortener.model.entity.UrlEntity;
import org.springframework.data.repository.CrudRepository;

public interface UrlRedisRepository extends CrudRepository<UrlEntity, String> {
}
