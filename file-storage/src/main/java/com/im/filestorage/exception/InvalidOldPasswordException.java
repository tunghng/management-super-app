package com.im.filestorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOldPasswordException extends BadRequestException {

    private static final long serialVersionUID = 1L;

    public InvalidOldPasswordException(String message) {
        super(message);
    }
}
