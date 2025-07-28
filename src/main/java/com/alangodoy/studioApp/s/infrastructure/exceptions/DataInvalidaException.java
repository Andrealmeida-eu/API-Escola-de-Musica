package com.alangodoy.studioApp.s.infrastructure.exceptions;


public class DataInvalidaException extends RuntimeException {
    public DataInvalidaException(String message) {
        super(message);
    }
}