package com.bncrypted.authenticator.exception;

import com.bncrypted.authenticator.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthenticatorResponseEntityExceptionHandlerTest {

    private AuthenticatorResponseEntityExceptionHandler handler = new AuthenticatorResponseEntityExceptionHandler();

    @Test
    void invalidCredentialsExceptionShouldReturnInvalidCredentialsMessage() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Invalid credentials");

        ErrorResponse expectedErrorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,
                "Invalid credentials", "timestamp");
        ErrorResponse actualErrorResponse = handler.handleInvalidCredentialsException(ex).getBody();

        assertErrorResponse(expectedErrorResponse, actualErrorResponse);
    }

    @Test
    void invalidTokenExceptionShouldReturnInvalidTokenMessage() {
        InvalidTokenException ex = new InvalidTokenException();

        ErrorResponse expectedErrorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED,
                "Invalid token", "timestamp");
        ErrorResponse actualErrorResponse = handler.handleInvalidTokenException(ex).getBody();

        assertErrorResponse(expectedErrorResponse, actualErrorResponse);
    }

    @Test
    void internalServerErrorShouldReturnInternalServerErrorMessage() {
        RuntimeException ex = new RuntimeException("Internal error");

        ErrorResponse expectedErrorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal error", "timestamp");
        ErrorResponse actualErrorResponse = handler.handleInternalServerErrors(ex).getBody();

        assertErrorResponse(expectedErrorResponse, actualErrorResponse);
    }

    @Test
    void nestedInternalServerErrorShouldReturnInternalServerErrorMessageForRootCause() {
        RuntimeException rootEx = new RuntimeException("Root internal error");
        RuntimeException ex = new RuntimeException("Internal error", rootEx);

        ErrorResponse expectedErrorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Root internal error", "timestamp");
        ErrorResponse actualErrorResponse = handler.handleInternalServerErrors(ex).getBody();

        assertErrorResponse(expectedErrorResponse, actualErrorResponse);
    }

    private void assertErrorResponse(ErrorResponse expectedErrorResponse, ErrorResponse actualErrorResponse) {
        assertNotNull(actualErrorResponse);
        assertAll("errorResponse",
                () -> assertEquals(expectedErrorResponse.getStatus(), actualErrorResponse.getStatus()),
                () -> assertEquals(expectedErrorResponse.getMessage(), actualErrorResponse.getMessage()),
                () -> assertNotNull(actualErrorResponse.getTimestamp())
        );
    }
}
