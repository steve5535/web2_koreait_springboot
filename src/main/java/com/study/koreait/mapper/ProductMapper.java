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

    /*
        페이지네이션 - 전체 데이터를 한번에 조회하는 것(풀 스캔) - x
        무한스크롤 - 스크롤이 다 내려가면, 기존내용 + 다음 페이지 내용
    */
    List<Product> findPage(
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 전체 페이지 수 계산용
    long countAll();

}
