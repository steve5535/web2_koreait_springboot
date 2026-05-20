-- day2_select

-- 전체 조회
select
	* -- 컬럼이름들, *은 모든 컬럼의미
from
	product; -- 테이블 or 가상테이블

-- as(별칭) 결과 컬럼 이름을 바꿀 수 있음
select
	product_name as productName 
from
	product;
    
-- where절 - if문

select
	*
from
	product
where
	product_id = 4; -- 정확히 일치

select * from product where product_id <> 4; -- !=와 같은동작
select * from product where price >= 3000;
-- between 연산: ~이상 ~이하
select * from product where price between 3000 and 4000;
-- price >= 3000 and price <= 4000 와 동일하다.

select 
	* 
from 
	product 
where 
	create_at between '2026-05-19 00:00:00' and '2026-05-21 00:00:00';

-- in 연산 "이중에 하나라도 맞으면" (or의 단축형)
select * from product where product_name in ('크로와상', '베이글');

-- like 연산 "패턴매칭"
select * from product where product_name like '%라떼%'; -- '라떼'가 들어간 모든 것
select * from product where product_name like '카%'; -- '카'로 시작하는 모든 것
-- b-tree 구조때문에 ~로 시작하는 검색이 빠르다.
select * from product where product_name like '__쿠키'; -- 정확히2글자 + '쿠키' 인 것

-- null을 다루는 연산
-- null은 = or != 연산 사용할 수 없다.
select * from post where content is null;
select * from post where content is not null;

-- order by - 정렬
select * from product order by price asc; -- 오름차순(기본)
select * from product order by price desc; -- 내림차순

-- 동률일때, 추가 기준 작성 가능
select 
	* 
from 
	product 
order by 
	price desc,
    product_name asc;

-- limit / offset
-- limit n: 위에서부터 n개만 보여줘
-- limit n offset m: m개 건너뛰고 위에서 n개만 보여줘
-- 페이지네이션에서 한번에 n개 게시글을 보여줄 때
-- limit n offset n * (page - 1)

select * from product order by price desc limit 3;
select
	*
from
	post
order by
	create_at desc
limit 2 offset 2; -- 최신순, 2개씩 볼때 2페이지 게시글들

-- distinct 중복되는 row 제거
select distinct
	price
from
	product
order by
	price;
    

# bit.ly/koreait-web
INSERT INTO post (title, content, create_at) VALUES
  ('첫 글입니다',           '블로그를 새로 시작했습니다. 잘 부탁드려요!',                  '2025-09-01 10:00:00'),
  ('오늘의 일기',           '오늘은 비가 와서 하루 종일 집에 있었다. 라면을 끓여 먹었다.', '2025-09-05 21:30:00'),
  ('강남 맛집 추천 3곳',     '1. A 식당  2. B 카페  3. C 떡볶이. 모두 가성비 좋아요.',   '2025-09-12 18:00:00'),
  ('책 리뷰: 모순',          '양귀자 작가의 모순을 읽었다. 인생에 대해 다시 생각하게 됨.', '2025-09-20 22:00:00'),
  ('부산 여행 후기',         '광안리 야경이 정말 멋졌다. 회도 신선했고.',                 '2025-10-01 12:00:00'),
  ('MySQL 공부중',           NULL,                                                       '2025-10-08 09:00:00'),
  ('영화 감상',              '어제 본 영화는 평점에 비해 별로였다. 너무 길었음.',          '2025-10-15 23:00:00'),
  ('짧은 메모',              '내일까지 보고서 마무리할 것.',                              '2025-10-25 08:00:00');

-- 1) title에 '리뷰'가 포함된 게시글들을 조회해 주세요
select * from post where title like '%리뷰%';

-- 2) content가 null이 아닌 게시글들 중에 오래된 순으로 3개만 조회해 주세요
select 
	* 
from 
	post 
order by
	create_at asc
limit
	3;

-- 3) post에서 가장 최신순으로 title만 3개만 조회하고, 컬럼이름을 '제목'으로 조회
select
	title as '제목'
from
	post
order by
	create_at desc
limit
	3;
    
/*
select 실행순서
	1) from - 어느테이블?
	2) where - 어떤 행만?
	3) select - 어떤 컬럼만?
	4) order by - 정렬
	5) limit offset - 몇 개?
-- where가 order by보다 먼저다
-> 걸러지고 정렬!
*/
	