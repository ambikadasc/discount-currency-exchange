package com.shop.discount.currencyexchange.errorhandling;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleInvalidCurrencyException() {
        InvalidCurrencyException ex = new InvalidCurrencyException("Invalid currency code: XYZ");
        ResponseEntity<String> response = exceptionHandler.handleInvalidCurrencyException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid currency code: XYZ", response.getBody());
    }

    @Test
    public void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Internal server error");
        ResponseEntity<String> response = exceptionHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }
}