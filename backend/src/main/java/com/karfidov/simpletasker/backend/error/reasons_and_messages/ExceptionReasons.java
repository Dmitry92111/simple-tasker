package com.karfidov.simpletasker.backend.error.reasons_and_messages;

public class ExceptionReasons {
    private ExceptionReasons() {
    }

    public static final String INCORRECT_REQUEST = "Incorrectly made request.";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error.";
    public static final String DATA_CONFLICT = "Data conflict.";
    public static final String NOT_FOUND = "The required object was not found.";
    public static final String FORBIDDEN_OPERATION = "Forbidden operation.";
    public static final String CONDITIONS_NOT_MET = "For the requested operation the conditions are not met.";
}
