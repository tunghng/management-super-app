package com.im.announcement.exception;

import com.im.announcement.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    @ResponseStatus(value = HttpStatus.OK)
    public Response handleOk(NotFoundException exception) {
        return new Response(exception.getLocalizedMessage());
    }

    @ExceptionHandler({BadRequestException.class, BindException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Response handleRequestException(Exception exception) {
        return new Response(400, exception.getLocalizedMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Response handleNotFoundException(NotFoundException exception) {
        return new Response(404, exception.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleAllException(Exception exception) {
        return new Response(500, exception.getLocalizedMessage());
    }
}
