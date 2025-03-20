package io.robothouse.urlshortener.lib.logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLogger extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLogger.class);
    private static final String REQUEST_ID_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID_KEY, requestId);
        logger.info("Incoming request: {} {}", req.getMethod(), req.getRequestURI());

        try {
            filterChain.doFilter(req, res);
        } finally {
            // Remove the request ID from MDC to avoid memory leaks
            MDC.remove(REQUEST_ID_KEY);
        }
    }
}
