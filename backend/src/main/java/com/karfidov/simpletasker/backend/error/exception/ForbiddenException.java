package com.karfidov.simpletasker.backend.error.exception;

public class ForbiddenException extends BaseApplicationException {
    public ForbiddenException(String message) {
        super(message);
    }
}
