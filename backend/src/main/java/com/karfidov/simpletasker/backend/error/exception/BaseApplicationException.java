package com.karfidov.simpletasker.backend.error.exception;

import lombok.Getter;

@Getter
public abstract class BaseApplicationException extends RuntimeException {
    protected BaseApplicationException(String message) {
        super(message);
    }
}
