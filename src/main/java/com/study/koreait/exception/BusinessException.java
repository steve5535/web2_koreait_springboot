package com.study.koreait.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 상속용으로 만들어진 비즈니스 예외 조상
// 핸들러 하나만 선언해서 사용하기 위함
// 부모 타입 핸들러가 자식 예외 타입까지 잡아 줌

@Getter
public abstract class BusinessException extends RuntimeException {
    private final HttpStatus statusCode;

    public BusinessException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
