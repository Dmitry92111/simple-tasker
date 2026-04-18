package com.karfidov.simpletasker.backend.error;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;


import java.time.Instant;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
public class ExceptionResponse {
    private final String status;
    private final String reason;
    private final String message;

    private final Instant timestamp;

    @JsonInclude(NON_EMPTY)
    private final List<String> errors;

    public ExceptionResponse(String status,
                             String reason,
                             String message,
                             Instant timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = List.of();
    }

    public ExceptionResponse(String status,
                             String reason,
                             String message,
                             Instant timestamp,
                             List<String> errors) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;

        if (errors != null) {
            this.errors = errors.isEmpty() ? List.of() : List.copyOf(errors);
        } else {
            this.errors = List.of();
        }
    }
}
