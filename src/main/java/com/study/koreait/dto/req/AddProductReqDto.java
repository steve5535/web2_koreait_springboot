package com.study.koreait.dto.req;

import com.study.koreait.entity.Product;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddProductReqDto {
    // 상품이름은 비울수x, 상품명 20자
    // price는 최소 1000원
    // stock은 음수면 x
    @NotBlank(message = "상품명을 비울 수 없습니다")
    @Size(max = 20, message = "상품명은 20자 이내여야 합니다")
    private String productName;

    @Min(value = 1000, message = "최소금액은 1000원 이상이어야 합니다")
    private int price;

//    @PositiveOrZero(message = "재고는 0개 이상이여야 합니다")
//    private int stock;

    // dto -> entity 변환책임을 dto에게 전가
    public Product toEntity() {
        return Product.builder()
                .productName(productName)
                .price(price)
                .build();
    }

}
