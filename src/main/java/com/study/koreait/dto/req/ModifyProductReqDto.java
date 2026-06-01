package com.study.koreait.dto.req;

import com.study.koreait.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModifyProductReqDto {
    private String productName;
    private int price;

    public Product toEntity(int id) {
        return Product.builder()
                .productId(id)
                .productName(productName)
                .price(price)
                .build();
    }
}
