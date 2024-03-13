package com.im.form.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(MapperException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> mapperExceptionHandler(MapperException exc) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        exc.getMessage(),
                        new Date())
                );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandler(BadRequestException exc) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        exc.getMessage(),
                        new Date())
                );
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> unauthorizedExceptionHandler(UnAuthorizedException exc) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED.value())
                .body(new ErrorResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        exc.getMessage(),
                        new Date())
                );
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> notPermissionExceptionHandler(ForbiddenException exc) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN.value())
                .body(new ErrorResponse(
                        HttpStatus.FORBIDDEN.value(),
                        exc.getMessage(),
                        new Date())
                );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundException exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND.value())
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        exc.getMessage(),
                        new Date())
                );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ErrorResponse(
                        HttpStatus.EXPECTATION_FAILED.value(),
                        exc.getMessage(),
                        new Date())
                );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandler(Exception exc) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        exc.getMessage(),
                        new Date())
                );
    }
}
