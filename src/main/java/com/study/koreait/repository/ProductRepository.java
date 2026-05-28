package com.study.koreait.repository;

import com.study.koreait.entity.Product;

import java.util.List;

// 추상체를 정의해서 변경에 용이하게 만들어보자
// Service가 구체적인 class 타입을 의존하는게 아닌
// interface를 의존하게 하는 전략
public interface ProductRepository {

    // 전체 상품 조회
    List<Product> findAllProducts();

    // 특정 id의 상품 조회
    Product findProductById(int id);

    // 새 상품 등록
    int insertProduct(Product product);

    // 단건 삭제
    int deleteProductById(int id);

    // 수정
    int updateProductById(Product product);

    // 검색
    List<Product> searchProductByName(String name);


}
