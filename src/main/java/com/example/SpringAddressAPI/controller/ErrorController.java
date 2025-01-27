package com.example.SpringAddressAPI.controller;

import com.example.SpringAddressAPI.dto.WebResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constrainViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>>responseStatusException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().message(exception.getReason()).build());
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<WebResponse<String>> missingRequestParam(MissingServletRequestParameterException exception){
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<WebResponse<String>> entityNotFound(EntityNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(WebResponse.<String>builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<WebResponse<String>> runtimeEx(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<String>builder()
                        .message("An unexpected error occurred: " + e.getMessage())
                        .build());
    }


}
