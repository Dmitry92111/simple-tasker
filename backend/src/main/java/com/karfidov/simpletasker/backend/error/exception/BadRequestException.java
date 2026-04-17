package com.karfidov.simpletasker.backend.error.exception;

public class BadRequestException extends BaseApplicationException {
    public BadRequestException(String message) {
        super(message);
    }
}
