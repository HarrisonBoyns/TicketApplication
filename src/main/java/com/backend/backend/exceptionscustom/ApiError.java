package com.backend.backend.exceptionscustom;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiError {

    private String message;

    private LocalDateTime localDateTime = LocalDateTime.now();

    private List<String> errors = null;

    public ApiError(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public ApiError(List<String> errors) {
        this.errors = errors;
    }

    public ApiError() {
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
