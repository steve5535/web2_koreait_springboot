package com.study.koreait.entity;

import com.study.koreait.dto.res.UserOrderPageResDto.OrderDetailResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetails {
    private int orderDetailId;
    private int orderId; // fk
    private int productId; // fk
    private int quantity;

    private Product product; // fk안쓸때 매핑할 그래프용 객체

    // dto 변환책임
    public OrderDetailResDto toOrderDetailResDto() {
        return OrderDetailResDto.builder()
                .productName(product.getProductName())
                .price(product.getPrice())
                .quantity(this.quantity)
                .build();
    }

}
