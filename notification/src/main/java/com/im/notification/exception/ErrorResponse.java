package com.im.notification.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private Integer status;
    private String message;
    private Date timestamp;

    public ErrorResponse() {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = "Internal Server Error";
        this.timestamp = new Date();
    }
}
