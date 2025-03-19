package io.robothouse.urlshortener.model.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.robothouse.urlshortener.lib.exception.HttpException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.SmartView;

import java.util.Map;

public record Fail(String requestId, String err, String status) implements BaseResponse, SmartView {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Fail(String requestId, String err) {
        this(requestId, err, "failed");
    }

    public Fail andHandleException(HttpServletResponse response, Throwable err) {
        if (err instanceof HttpException) {
            response.setStatus(((HttpException) err).getStatusCode());
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return this;
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public boolean isRedirectView() {
        return false;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());
        String json = objectMapper.writeValueAsString(this);
        response.getWriter().write(json);
    }
}
