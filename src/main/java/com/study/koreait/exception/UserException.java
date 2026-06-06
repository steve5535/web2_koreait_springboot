package com.study.koreait.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UserException extends RuntimeException {
    private HttpStatus status;
    public UserException(String message, HttpStatus code) {
        super(message);
        this.status = code;
    }
}
