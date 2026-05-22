# 서브쿼리
-- 쿼리 안에 쿼리
/*
	1) select절 서브쿼리 - 스칼라서브쿼리
    2) where절 서브쿼리
    3) from절 서브쿼리 - 인라인뷰
    4) 서브쿼리와 join
*/

-- select 서브쿼리: 단 하나의 값을 리턴하는 서브쿼리
select
	product_name,
    price,
    price - (select avg(price) from product) as 가격편차치
from
	product;

-- where절 서브쿼리
-- 평균보다 비싼 상품들만 보고싶다
select
	*
from
	product
where
	price > (select avg(price) from product);

-- in연산자와 서브쿼리 조합하면 조인으로 얻을 수 있는 결과를 얻을 수 있음
-- order_details에 product_id가 외래키로 존재하면, 팔린적 있는 것

select
	*
from
	product
where
	product_id not in (select distinct product_id from order_details);


-- 1) 댓글이 한번이라도 달린 게시글들 조회
select
	*
from
	post
where
	post_id in (select distinct post_id from comments);

-- 주의) in연산자는 = 비교연산을 or로 묶어놓은 연산
-- -> null과 연산할때 문제가 일어날 수 있음

# exists
-- 서브쿼리가 1row라도 돌려주면 true, 아니면 false
select
	*
from
	post p
where exists (
	select
		1
	from
		comments c
	where p.post_id = c.post_id -- 바깥 테이블이 안쪽 서브쿼리에 사용되면, 한줄씩 실행
); -- in과 동일하지만, null 위험이 적으므로 더 안전함


# from절 서브쿼리 -- 임시 테이블을 만들어서 사용
-- 쿼리 결과자체를 하나의 테이블로 취급
select
	p.product_name,
    s.판매집계
from
	product p inner join (
    select
		product_id,
		sum(quantity) as 판매집계
	from
		order_details
	group by
		product_id
	) s on p.product_id = s.product_id;

-- 1. post에서 content가 null이지만, 댓글은 달린 게시글들을 조회해 주세요
-- exists 활용해보세요
select
	*
from
	post p
where
	p.content is null
    and exists (
		select 1 from comments c where p.post_id = c.post_id
    );

-- 2. 댓글이 가장 많이 달린 게시글을 조회
-- where절에 스칼라 서브쿼리 활용
select
	*
from
	post
where
	post_id = (
    select
		post_id
	from
		comments
	group by
		post_id
	order by
		count(*) desc
	limit 1
);

