package io.robothouse.urlshortener.controller.advice;

import io.robothouse.urlshortener.lib.exception.BadRequestException;
import io.robothouse.urlshortener.lib.exception.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

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
        log.error(
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
