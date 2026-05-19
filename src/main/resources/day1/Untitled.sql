-- database 생성
create database koreait_web2;
-- database 사용
use koreait_web2;

/*
데이터베이스의 자료형
INT -> int / Integer
VARCHAR(n) -> String : 최대문자수 지정
TEXT -> String : 길이 무제한
DATETIME -> LocalDataTime : 날짜+시간 '2026-05-19 21:31:00'
DECIMAL(p,s) -> BigDecimal : 소수점 고정 30.12$
*/

-- 테이블(자바의 클래스와 1:1 대응) 생성
-- 제약사항
create table product(
	product_id int not null auto_increment,
    product_name varchar(50) not null,
    price int not null,
    create_at datetime not null default current_timestamp,
    primary key(product_id)
);
/*
제약사항 & 기타
not null -> 비워두면 안된다
primary key -> 이 row를 다른 모든 row와 구분짓는 컬럼
default -> insert할때 비워두면, 기본값으로 써라

*/