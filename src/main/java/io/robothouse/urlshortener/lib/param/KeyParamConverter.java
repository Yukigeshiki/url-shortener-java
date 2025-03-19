package io.robothouse.urlshortener.lib.param;

import io.robothouse.urlshortener.model.Key;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class KeyParamConverter implements Converter<String, Key> {

    @Override
    public Key convert(String source) {
        return new Key(source);
    }
}
