package com.company.taskservice.infrastructure.web;

import com.company.taskservice.infrastructure.exception.MyEntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final URI ERRORS_VALIDATION_TYPE = URI.create("/errors/validation");
    private static final URI ERRORS_NOT_FOUND_TYPE = URI.create("/errors/not-found");
    private static final URI ERRORS_INTERNAL_TYPE = URI.create("/errors/internal");

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        logger.error("400 Status Code", ex);
        String globalErrorsMsg = ex.getBindingResult().getGlobalErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        String fieldErrorsMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        StringJoiner allErrorsMsgJoiner = new StringJoiner(", ");
        if (!globalErrorsMsg.isEmpty()) {
            allErrorsMsgJoiner.add(globalErrorsMsg);
        }
        if (!fieldErrorsMsg.isEmpty()) {
            allErrorsMsgJoiner.add(fieldErrorsMsg);
        }
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, allErrorsMsgJoiner.toString());
        problemDetail.setType(ERRORS_VALIDATION_TYPE);

        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ MyEntityNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(
            MyEntityNotFoundException ex,
            WebRequest request) {
        logger.error("404 Status Code", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(ERRORS_NOT_FOUND_TYPE);

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        logger.error("500 Status Code", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problemDetail.setType(ERRORS_INTERNAL_TYPE);

        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
