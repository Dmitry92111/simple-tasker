package com.karfidov.simpletasker.backend.error.reasons_and_messages;

import java.util.Map;

public class ExceptionMessages {
    private ExceptionMessages() {
    }

    public static final String DEFAULT_FIELD_S_ERROR_S_VALUE_S_MESSAGE =
            "Field: %s. Error: %s. Value: %s";

    public static final String START_DATE_AFTER_END_DATE = "Start date cannot be after end date";

    //Incorrect request
    public static final String VALIDATION_FAILED = "Validation failed.";
    public static final String INCORRECT_HTTP_REQUEST_BODY = "Request body is invalid or malformed.";
    public static final String MISMATCH_OF_TYPES_OF_PARAMETER_OF_REQUEST_AND_METHOD_ARGUMENT =
            "Parameter '%s' has invalid value '%s'.";
    public static final String MISSING_REQUIRED_PARAMETER_OF_HTTP_REQUEST = "Required parameter: '%s' is missing.";
    public static final String INCORRECT_REQUEST = "Incorrect request.";

    //Internal server error
    public static final String INTERNAL_SERVER_ERROR = "Something went wrong";


    //DataBase exceptions
    public static final String DATA_INTEGRITY_VIOLATION = "The operation violates data integrity rules.";
    public static final String INTEGRITY_CONSTRAINT_VIOLATED = "Integrity constraint has been violated.";
    public static final Map<String, String> CVE_CONSTRAINT_TO_MESSAGE = Map.of(
            "users_email_uq_active", "Email already exists"
    );


    //User
    public static final String USER_NOT_FOUND = "User with id=%d was not found";

    //Task
    public static final String TASK_NOT_FOUND = "Task with id=%d was not found";
    public static final String TASK_IS_ALREADY_STARTED_OR_DONE = "Sorry, but this task has already been started or done";
    public static final String TASK_IS_NOT_IN_PROGRESS = "Sorry, but only tasks in progress can be marked as done";


}
