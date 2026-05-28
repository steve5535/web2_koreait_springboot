package com.study.koreait.mapper;

import com.study.koreait.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    // 전체 상품 조회
    List<Product> findAllProducts();

    // 특정 id의 상품 조회
    Product findProductById(int id);

    // 새 상품 등록
    int insertProduct(Product product);

    // 단건 삭제
    int deleteProductById(int id);

    // 이름 검색
    List<Product> searchProductByName(String name);

    // 상세 검색 - name필터 + 가격필터
    List<Product> detailSearchProduct(
            @Param("nameKeyword") String nameKeyword,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );

    // 선택 검색 - 가격필터
    List<Product> chooseSearchProduct(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );

    // 컬렉션 파라미터는 @Param으로 명시해야 callection="products"로 받을 수 있음
    int insertProducts(@Param("products") List<Product> products);

}
