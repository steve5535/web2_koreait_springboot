package com.study.koreait.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StudentException extends BusinessException {
    public StudentException(String message, HttpStatus statusCode) {
        super(message, statusCode);
    }
}
