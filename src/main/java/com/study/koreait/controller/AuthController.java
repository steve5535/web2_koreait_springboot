package com.study.koreait.controller;

import com.study.koreait.dto.req.SignUpReqDto;
import com.study.koreait.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    // 회원가입 signup
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpReqDto dto) {
        authService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("계정생성완료");
    }

    // 로그인 signIn
    // HTTPS를 쓰면 body는 암호화되서 가로채도 볼 수 없음
    // GET 요청은 url은 암호화되지 않기때문에 가로채서 볼 수 있음
    @PostMapping("/signin")
    public ResponseEntity<?> signIn() {
        return ResponseEntity.ok("jwt 토큰");
    }

}
