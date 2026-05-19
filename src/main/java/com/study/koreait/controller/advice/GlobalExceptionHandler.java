package com.study.koreait.controller.advice;

// 전역 예외 처리 핸들러
// 1. dispatcherServlet한테 전역으로 try-catch 존재
// 2. 예외는 계속해서 호출한쪽으로 전파된다.
// -> dispatcherServlet이 catch로 예외를 잡을 수 있음.
// -> 해당예외를 처리할 수 있는 핸들러 컨트롤러를 찾는다.

import com.study.koreait.exception.ProductException;
import com.study.koreait.exception.StudentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(MethodArgumentNotValidException e) {
        /*
            [
                {
                    "필드이름": "예외메세지"
                },
                {
                    "필드이름: "예외메세지"
                }
            ]
        */
        List<Map<String, String>> errorMap = null;

        BindingResult br = e.getBindingResult();

        if (br.hasErrors()) {
            errorMap = br.getFieldErrors() // 필드에러 객체들을 리스트로 반환
                    .stream()
                    .map(fe -> {
                        Map<String, String> feMap = new HashMap<>();
                        feMap.put(fe.getField(), fe.getDefaultMessage());
                        return feMap;
                    })
                    .toList();
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMap);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<?> handleProductException(ProductException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getMessage());
    }

    @ExceptionHandler(StudentException.class)
    public ResponseEntity<?> handleStudentException(StudentException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(e.getMessage());
    }

}
