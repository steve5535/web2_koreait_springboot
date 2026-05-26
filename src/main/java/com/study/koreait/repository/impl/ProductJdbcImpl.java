package com.study.koreait.repository.impl;

import com.study.koreait.entity.Product;
import com.study.koreait.exception.ProductException;
import com.study.koreait.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Primary // 같은 타입의 bean이 여러개일때, "이걸먼저 주입하라"라는 우선권
@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductJdbcImpl implements ProductRepository {

    // sting-boot-starter-jdbc 라이브러리가 빈등록까지 해 줌
    // yaml에 작성했던 DataSource를 주입해줌
    private final DataSource dataSource;

    // rs를 Product객체로 조립해주는 메서드
    private Product rsToProduct(ResultSet rs) throws SQLException {
        return Product.builder()
                .productId(rs.getInt("product_id"))
                .productName(rs.getString("product_name"))
                .price(rs.getInt("price"))
                .createAt(rs.getTimestamp("create_at").toLocalDateTime())
                .build();
    }

    @Override
    public List<Product> findAllProducts() {
        String sql = "select * from product";

        // jdbc사용시 중요했던 3가지 객체
        Connection conn = null; // db와의 연결 빌려오는 객체
        PreparedStatement ps = null; // 쿼리 조립 & 실행 객체
        ResultSet rs = null; // 쿼리 결과를 받아주는 객체
        List<Product> products = new ArrayList<>();

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(); // 쿼리 실행 -> ResultSet으로 결과 받아오기

            // rs는 필드로 전체 테이블 값을 가지고 있음
            // 한줄씩 꺼내와서 보여준다.
            // rs.next(): 다음 row가 있으면 true, 없으면 false
            // true를 리턴한다면 다음행으로 '커서' 이동
            while(rs.next()) {
                Product p = rsToProduct(rs);
                products.add(p);
            }

            return products;

        } catch (SQLException e) {
            log.error("findAllProducts 실패", e);
            throw new ProductException("상품조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            // 자원반납
            // 가져온 순서의 역순으로 반납
            if(rs != null) try {rs.close();} catch (SQLException e) {log.info("rs반납실패", e);}
            if(ps != null) try {ps.close();} catch (SQLException e) {log.info("ps반납실패", e);}
            if(conn != null) try {conn.close();} catch (SQLException e) {log.info("conn반납실패", e);}

        }

    }

    // 단건조회
    @Override
    public Product findProductById(int id) {
        // sql injection 방어를 위해 미리 준비되어있는 메서드를 사용하낟.
        String sql = "select * from product where product_id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            // sql문자열에서 왼쪽부터 읽었을때 1번째 발견되는 ?에 id값 넣으세요
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                // 데이터 있음
                return rsToProduct(rs);
            } else {
                // 데이터 한줄도 없을 때
                throw new ProductException("해당 id의 상품은 존재하지 않습니다", HttpStatus.NOT_FOUND);
            }

        } catch (SQLException e) {
            log.error("findProductById 실패 - id={}", id, e);
            throw new ProductException("상품조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if(rs != null) try {rs.close();} catch (SQLException e) {log.info("rs반납실패", e);}
            if(ps != null) try {ps.close();} catch (SQLException e) {log.info("ps반납실패", e);}
            if(conn != null) try {conn.close();} catch (SQLException e) {log.info("conn반납실패", e);}
        }


    }

    @Override
    public int insertProduct(Product product) {
        return 0;
    }

    @Override
    public int deleteProductById(int id) {
        return 0;
    }

    @Override
    public int updateProductById(Product product) {
        return 0;
    }

    // 아래의 메서드를 jdbc코드로 완성기켜 주세요
    // ps.setString()할때 조립
    @Override
    public List<Product> searchProductByName(String name) {
        String sql = "select * from product where product_name like ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);

            // sql injection 공격 방어
            // 이스케이프 문제로 부터 자유롭기 위해
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                products.add(rsToProduct(rs));
            }

            return products;
        } catch (SQLException e) {
            log.error("searchProductByName 실패 - name={}", name, e);
            throw new ProductException("상품 검색 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if(rs != null) try {rs.close();} catch (SQLException e) {log.info("rs반납실패", e);};
            if(ps != null) try {ps.close();} catch (SQLException e) {log.info("ps반납실패", e);};
            if(conn != null) try {conn.close();} catch (SQLException e) {log.info("conn반납실패", e);};
        }
    }
}
