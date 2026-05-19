package com.study.koreait.repository.impl;

import com.study.koreait.entity.Product;
import com.study.koreait.exception.ProductException;
import com.study.koreait.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class ProductRepoImpl implements ProductRepository {

    // DB 대용 데이터
    private final List<Product> products = new ArrayList<>(
            Arrays.asList(
                    Product.builder().productId(1).productName("노트북").price(1900000).build(),
                    Product.builder().productId(2).productName("마우스").price(30000).build(),
                    Product.builder().productId(3).productName("키보드").price(80000).build(),
                    Product.builder().productId(4).productName("카메라").price(1500000).build()
            )
    );

    // 전체 데이터 조회 -> db를 풀스캔하는 건 없어야 함.
    // 참고) 페이지네이션으로 조회하게끔 변경해야함
    @Override
    public List<Product> findAllProducts() {
        return products;
    }

    // 단건조회
    @Override
    public Product findProductById(int id) {
        // DB쿼리가 되어야함
        Optional<Product> optProduct = products.stream()
                .filter(p -> p.getProductId() == id)
                .findFirst();

        if (optProduct.isEmpty()) {
            throw new ProductException("해당 id의 상품은 존재하지 않습니다", HttpStatus.NOT_FOUND);
        }

        return optProduct.get(); // null이 아닐때만 리턴
    }

    @Override
    public int insertProduct(Product product) {
        // 차후에는 쿼리로 대체
        // id 최댓값을 찾아야함 - autoincrement 대체
        int maxId = 0;
        for (Product p : products) {
            int productId = p.getProductId();
            if (productId > maxId) {
                maxId = productId;
            }
        }

        Product newProduct = Product.builder()
                .productId(maxId + 1)
                .productName(product.getProductName())
                .price(product.getPrice())
                .build();

        products.add(newProduct);

        return 1; // db에서도 단건추가에 대해서 1리턴함
    }

    @Override
    public int deleteProductById(int id) {
        // 매개변수로 들어온 id가 유효한지?
        Optional<Product> optProduct = products.stream()
                .filter(p -> p.getProductId() == id)
                .findFirst();

        Product product = optProduct.orElseThrow(() -> new ProductException("해당 id의 상품은 존재하지 않습니다", HttpStatus.NOT_FOUND));

        products.remove(product);
        log.info("상품 삭제 완료: {}", product);

        return 1; // db에서도 단건삭제의 경우 1리턴
    }

    // PATCH - 부분수정
    // PUT - 전체수정
    @Override
    public int updateProductById(Product product) {
        // id가 실제 있는 id인지 유효한지
        int productId = product.getProductId();
        Optional<Product> optProduct = products.stream()
                .filter(p -> p.getProductId() == productId)
                .findFirst();

        if (optProduct.isEmpty()) {
            throw new ProductException("유효하지 않는 접근입니다", HttpStatus.BAD_REQUEST);
        }

        // 리스트 업데이트
        // 리스트.set(index, 저장할데이터);

        // filter로 id가 동일한 객체 찾아서 index 추출
        int index = products.indexOf(optProduct.get());
        // 해당 index에 매개변수로 들어온 객체로 덮어쓰기
        products.set(index, product);


        return 1;
    }

    @Override
    public List<Product> searchProductByName(String name) {
        // 선택) 예외를 던지거나 or 빈리스트를 리턴하거나
        List<Product> returnData = products.stream()
                .filter(p -> p.getProductName().contains(name))
                .toList();

        return List.of();
    }
}
