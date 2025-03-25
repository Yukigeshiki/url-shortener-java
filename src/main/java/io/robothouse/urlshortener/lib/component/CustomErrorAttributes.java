package io.robothouse.urlshortener.lib.component;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    private static final String PATH_KEY = "path";
    private static final String TRACE_KEY = "trace";
    private static final String TIMESTAMP_KEY = "timestamp";

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        // remove unnecessary error attributes
        errorAttributes.remove(PATH_KEY);
        errorAttributes.remove(TRACE_KEY);
        errorAttributes.remove(TIMESTAMP_KEY);

        return errorAttributes;
    }
}
