package com.backend.backend.exceptionscustom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ExceptionHandlerTickets {

    @ResponseBody
    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<ApiError> handleApiException(ApiException ex){

        HttpStatus status = ex.getResponseCode();

        List<String> errorMessages = new ArrayList<>();

        String message = null;

        if(status.equals(HttpStatus.BAD_REQUEST)){
            message = HttpStatus.BAD_REQUEST.toString();
        }
        else if(status.equals(HttpStatus.UNAUTHORIZED)){
            message = HttpStatus.BAD_REQUEST.toString();
        }
        else if(status.equals(HttpStatus.FORBIDDEN)){
            message = HttpStatus.FORBIDDEN.toString();
        }
        else if(status.equals(HttpStatus.NOT_FOUND)){
            message = HttpStatus.NOT_FOUND.toString();
        }
        else if(status.equals(HttpStatus.INTERNAL_SERVER_ERROR)){
            message = HttpStatus.INTERNAL_SERVER_ERROR.toString();
        }
        else if(status.equals(HttpStatus.NOT_IMPLEMENTED)){
            message = HttpStatus.NOT_IMPLEMENTED.toString();
        }
        else {
            message = HttpStatus.INTERNAL_SERVER_ERROR.toString();
        }
        errorMessages.add(message);
        return new ResponseEntity<ApiError>(new ApiError(ex.getLocalizedMessage(), errorMessages), ex.getResponseCode());
    }

    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleApiException(ConstraintViolationException ex, WebRequest request) {
        List<String> errorMessages = new ArrayList<>();
        log.warn(request.getDescription(true));
        return new ResponseEntity<ApiError>(new ApiError(ex.getLocalizedMessage(), errorMessages), HttpStatus.BAD_REQUEST);
    }

    }
