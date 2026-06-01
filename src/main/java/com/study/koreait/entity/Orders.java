package com.study.koreait.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Orders {
    private int orderId;
    private LocalDateTime orderedAt;
    private String userId; // fk

    private Users user; // fk안쓸때 사용하는 객체매핑
    private List<OrderDetails> orderDetails;


}
