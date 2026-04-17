package com.karfidov.simpletasker.backend.error;

import com.karfidov.simpletasker.backend.error.exception.BadRequestException;
import com.karfidov.simpletasker.backend.error.exception.ConditionsNotMetException;
import com.karfidov.simpletasker.backend.error.exception.ForbiddenException;
import com.karfidov.simpletasker.backend.error.exception.NotFoundException;
import com.karfidov.simpletasker.backend.error.reasons_and_messages.ExceptionMessages;
import com.karfidov.simpletasker.backend.error.reasons_and_messages.ExceptionReasons;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //@Valid exceptions
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleValidationExceptions(Exception ex,
                                                        HttpServletRequest req) {
        BindingResult bindingResult;

        if (ex instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
        } else {
            bindingResult = ((BindException) ex).getBindingResult();
        }

        log4xx(HttpStatus.BAD_REQUEST, req, "validation errors=%d", bindingResult.getFieldErrorCount());
        List<String> errorMessages = bindingResult.getFieldErrors().stream().map(this::buildFieldMessage).toList();

        String message = (errorMessages.size() == 1) ? errorMessages.getFirst() : ExceptionMessages.VALIDATION_FAILED;

        List<String> errors = (errorMessages.size() > 1) ? errorMessages : List.of();

        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST.name(),
                ExceptionReasons.INCORRECT_REQUEST,
                message,
                errors
        );
    }

    //incorrect RequestParam/PathVariable with @Validated
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleParamValidationExceptions(jakarta.validation.ConstraintViolationException ex,
                                                             HttpServletRequest req) {
        var violation = ex.getConstraintViolations().stream().findFirst().orElse(null);

        String message = (violation == null)
                ? ExceptionMessages.VALIDATION_FAILED
                : String.format(ExceptionMessages.DEFAULT_FIELD_S_ERROR_S_VALUE_S_MESSAGE,
                violation.getPropertyPath(),
                violation.getMessage(),
                violation.getInvalidValue()
        );

        log4xx(HttpStatus.BAD_REQUEST, req, message);

        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST.name(),
                ExceptionReasons.INCORRECT_REQUEST,
                message
        );
    }

    //incorrect DateTime format, enum, incorrect JSON
    //incorrectParamType
    //RequestParam required but not found
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadRequestExceptions(Exception ex,
                                                        HttpServletRequest req) {

        String message = switch (ex) {
            case HttpMessageNotReadableException _ ->
                    ExceptionMessages.INCORRECT_HTTP_REQUEST_BODY;
            case MethodArgumentTypeMismatchException mismatch -> String.format(
                    ExceptionMessages.MISMATCH_OF_TYPES_OF_PARAMETER_OF_REQUEST_AND_METHOD_ARGUMENT,
                    mismatch.getName(),
                    mismatch.getValue()
            );
            case MissingServletRequestParameterException missing -> String.format(
                    ExceptionMessages.MISSING_REQUIRED_PARAMETER_OF_HTTP_REQUEST,
                    missing.getParameterName()
            );
            case null, default -> ExceptionMessages.INCORRECT_REQUEST;
        };

        log4xx(HttpStatus.BAD_REQUEST, req, message);

        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST.name(),
                ExceptionReasons.INCORRECT_REQUEST,
                message
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleDataIntegrityExceptions(DataIntegrityViolationException ex,
                                                           HttpServletRequest req) {
        String message = ExceptionMessages.DATA_INTEGRITY_VIOLATION;

        var hibernateCve = findHibernateConstraintViolation(ex);
        if (hibernateCve != null) {
            String constraint = hibernateCve.getConstraintName();
            Map<String, String> map = ExceptionMessages.CVE_CONSTRAINT_TO_MESSAGE;

            if (constraint != null && map.containsKey(constraint)) {
                message = map.get(constraint);
                log4xx(HttpStatus.CONFLICT, req, "constraint=%s", constraint);
            } else {
                message = ExceptionMessages.INTEGRITY_CONSTRAINT_VIOLATED;
                log4xx(HttpStatus.CONFLICT, req,
                        "data integrity violation (unknown constraint): %s", safeMostSpecificMessage(ex));
            }
        } else {
            log4xx(HttpStatus.CONFLICT, req, "data integrity violation: %s", safeMostSpecificMessage(ex));
        }

        return new ExceptionResponse(
                HttpStatus.CONFLICT.name(),
                ExceptionReasons.DATA_CONFLICT,
                message
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadRequest(BadRequestException ex,
                                              HttpServletRequest req) {
        log4xx(HttpStatus.BAD_REQUEST, req, ex.getMessage());
        return new ExceptionResponse(
                HttpStatus.BAD_REQUEST.name(),
                ExceptionReasons.INCORRECT_REQUEST,
                ex.getMessage()
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFoundExceptions(NotFoundException ex,
                                                      HttpServletRequest req) {
        log4xx(HttpStatus.NOT_FOUND, req, ex.getMessage());
        return new ExceptionResponse(
                HttpStatus.NOT_FOUND.name(),
                ExceptionReasons.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(ConditionsNotMetException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleConditionsNotMetExceptions(ConditionsNotMetException ex,
                                                              HttpServletRequest req) {
        log4xx(HttpStatus.CONFLICT, req, ex.getMessage());
        return new ExceptionResponse(
                HttpStatus.CONFLICT.name(),
                ExceptionReasons.CONDITIONS_NOT_MET,
                ex.getMessage()
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleForbiddenExceptions(ForbiddenException ex,
                                                       HttpServletRequest req) {
        log4xx(HttpStatus.FORBIDDEN, req, ex.getMessage());
        return new ExceptionResponse(
                HttpStatus.FORBIDDEN.name(),
                ExceptionReasons.FORBIDDEN_OPERATION,
                ex.getMessage()
        );
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleUnexpectedExceptions(Throwable ex) {
        log.error("Unexpected error", ex);
        return new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ExceptionReasons.INTERNAL_SERVER_ERROR,
                ExceptionMessages.INTERNAL_SERVER_ERROR
        );
    }

    private String buildFieldMessage(FieldError fe) {
        return String.format(
                ExceptionMessages.DEFAULT_FIELD_S_ERROR_S_VALUE_S_MESSAGE,
                fe.getField(),
                fe.getDefaultMessage(),
                fe.getRejectedValue()
        );
    }

    private org.hibernate.exception.ConstraintViolationException findHibernateConstraintViolation(Throwable ex) {
        Throwable current = ex;
        while (current != null) {
            if (current instanceof org.hibernate.exception.ConstraintViolationException) {
                return (org.hibernate.exception.ConstraintViolationException) current;
            }
            current = current.getCause();
        }
        return null;
    }

    private String safeMostSpecificMessage(DataIntegrityViolationException ex) {
        var cause = ex.getMostSpecificCause();
        return cause.getMessage() != null ? cause.getMessage() : ex.getMessage();
    }

    private void log4xx(HttpStatus status, HttpServletRequest req, String template, Object... args) {
        String msg = String.format(template, args);
        log4xx(status, req, msg);
    }

    private void log4xx(HttpStatus status, HttpServletRequest req, String message) {
        String logMessage = String.format("%d %s | %s %s | %s",
                status.value(), status.name(), req.getMethod(), pathWithQuery(req), message);

        if (status == HttpStatus.CONFLICT) log.warn(logMessage);
        else log.info(logMessage);
    }

    private String pathWithQuery(HttpServletRequest req) {
        String qs = req.getQueryString();
        return (qs == null || qs.isBlank()) ? req.getRequestURI() : req.getRequestURI() + "?" + qs;
    }
}
