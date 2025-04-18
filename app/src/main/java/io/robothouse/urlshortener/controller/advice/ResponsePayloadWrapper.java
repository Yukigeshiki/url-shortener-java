package io.robothouse.urlshortener.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import lombok.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ResponsePayloadWrapper implements ResponseBodyAdvice<Object> {

    private static final String REQUEST_ID_KEY = "requestId";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String PAYLOAD_KEY = "payload";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // apply to all responses
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response
    ) {

        String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String requestId = null;
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            requestId = (String) servletRequest.getAttribute(REQUEST_ID_KEY);
        }
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put(TIMESTAMP_KEY, timestamp);
        wrapper.put(REQUEST_ID_KEY, requestId);
        wrapper.put(PAYLOAD_KEY, body);

        return wrapper;
    }
}
