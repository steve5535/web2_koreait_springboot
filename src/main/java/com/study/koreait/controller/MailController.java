package com.study.koreait.controller;


import com.study.koreait.service.MailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 화면(json이 아님)을 리턴해주는 컨트롤러
@Controller
@RequiredArgsConstructor
@RequestMapping("/mail")
@Slf4j
public class MailController {

    private final MailService mailService;

    @ResponseBody // @Controller에서 Json응답을 할려면 @ResponseBody필요
    @PostMapping("/send")
    public ResponseEntity<?> send(@AuthenticationPrincipal String userId) {
        log.info("userId={}", userId);
        return ResponseEntity.ok(mailService.sendVerifyMail(userId));
    }

    // rest 컨트롤러 메서드 x
    // 컨트롤러 메서드 String return하면 정적 html파일을 사용자에게 응답합
    @GetMapping("/verify")
    public String verify(Model model, @RequestParam String token) {
        // model에 데이터를 담아서 html쪽으로 데이터 전달 가능
        // ${status} -> 객체라면 status 필드값 or Map이라면 status의 value값
        // ${message} -> Map에서 message의 value값
        Map<String, Object> result = mailService.verify(token);
        model.addAllAttributes(result);
        return "mail-result";
    }

}
