package io.robothouse.urlshortener.lib.component;

import io.robothouse.urlshortener.model.UrlKeyPathVariable;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class KeyPathVariableConverter implements Converter<String, UrlKeyPathVariable> {

    @Override
    public UrlKeyPathVariable convert(@NonNull String source) {
        return new UrlKeyPathVariable(source);
    }
}
