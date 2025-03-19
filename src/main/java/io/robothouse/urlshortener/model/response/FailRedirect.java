package io.robothouse.urlshortener.model.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.SmartView;

import java.util.Map;

public record FailRedirect(String requestId, String err, String status) implements SmartView {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public FailRedirect(String requestId, String err) {
        this(requestId, err, "failed");
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
