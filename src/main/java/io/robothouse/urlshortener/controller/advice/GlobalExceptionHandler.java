package io.robothouse.urlshortener.controller.advice;

import io.robothouse.urlshortener.lib.exception.BadRequestException;
import io.robothouse.urlshortener.lib.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        Map<String, Object> response = buildResponseMap(e.getMessage(), e.getStatus());
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        Map<String, Object> response = buildResponseMap(e.getMessage(), e.getStatus());
        return new ResponseEntity<>(response, e.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Map<String, Object> response = buildResponseMap(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        logger.error(
                "Stacktrace for {}: {}", e.getCause() != null ? e.getCause() : "Unknown Error", e.getStackTrace()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static Map<String, Object> buildResponseMap(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", status.value());
        response.put("type", status.name());
        return response;
    }
}
