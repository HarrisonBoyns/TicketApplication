package com.backend.backend.exceptionscustom;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private ApiError apiError = new ApiError();

    private HttpStatus responseCode;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.responseCode = status;
        apiError.setMessage(message);
    }

    public ApiException() {
    }

    public HttpStatus getResponseCode() {
        return responseCode;
    }
}
