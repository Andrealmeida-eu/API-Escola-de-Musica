package com.alangodoy.studioApp.s.infrastructure.exceptions;

public class ConcurrencyException extends RuntimeException {
    public ConcurrencyException(String message) {
        super(message);
    }
}
