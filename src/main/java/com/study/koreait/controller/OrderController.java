package com.study.koreait.controller;

import com.study.koreait.dto.req.PeriodPageReqDto;
import com.study.koreait.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserOrdersHistory(
            @PathVariable String userId,
            PeriodPageReqDto dto
    ) {
        return ResponseEntity.ok(orderService.getUserOrderHistory(userId, dto));
    }

}
