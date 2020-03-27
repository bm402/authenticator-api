package com.bncrypted.authenticator.exception;

import com.bncrypted.authenticator.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticatorResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), getCurrentTimestamp());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

//    @ResponseBody
//    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class, RuntimeException.class })
//    public ResponseEntity<ErrorResponse> handleInternalServerErrors(RuntimeException ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), getCurrentTimestamp());
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    private String getCurrentTimestamp() {
        return Instant.now()
                .truncatedTo(ChronoUnit.SECONDS)
                .toString()
                .replaceAll("[TZ]", " ");
    }

}
