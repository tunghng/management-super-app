package com.im.filestorage.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.im.filestorage.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleServerException(HttpServerErrorException.InternalServerError ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Server error"
        ));
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(
                HttpStatus.BAD_REQUEST.value(),
                ex.getLocalizedMessage()
        ));
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(
                HttpStatus.NOT_FOUND.value(),
                ex.getLocalizedMessage()
        ));
    }

    @ExceptionHandler(value = UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Response> handleUnauthorizedException(UnAuthorizedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(
                HttpStatus.FORBIDDEN.value(),
                ex.getLocalizedMessage()
        ));
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleJsonProcessingException(JsonProcessingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getLocalizedMessage()
        ));
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getLocalizedMessage()
        ));
    }
}
