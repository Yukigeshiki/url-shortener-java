package io.robothouse.urlshortener.lib.converter;

import io.robothouse.urlshortener.model.url.UrlKeyPathVar;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UrlKeyPathVarConverter implements Converter<String, UrlKeyPathVar> {

    @Override
    public UrlKeyPathVar convert(String source) {
        return new UrlKeyPathVar(source);
    }
}
