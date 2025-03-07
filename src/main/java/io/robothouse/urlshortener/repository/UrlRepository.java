package io.robothouse.urlshortener.repository;

import io.robothouse.urlshortener.model.Url;
import org.springframework.data.repository.CrudRepository;

public interface UrlRepository extends CrudRepository<Url, String> {
}
