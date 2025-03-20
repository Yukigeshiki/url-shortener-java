package io.robothouse.urlshortener.lib.converter;

import io.robothouse.urlshortener.model.KeyPathVar;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class KeyPathVarConverter implements Converter<String, KeyPathVar> {

    @Override
    public KeyPathVar convert(String source) {
        return new KeyPathVar(source);
    }
}
