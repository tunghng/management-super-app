package com.im.announcement.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}