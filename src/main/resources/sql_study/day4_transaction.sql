# day4_transaction
# all or nothing
# 하나의 작업 단위
# 1쿼리 -> 2쿼리 -> 3쿼리
# 1~3 모두 실행됬거나 or 아예 실행이 안되거나

start transaction; -- 트랜잭션 실행

insert into
	orders (customer_name)
values
	('길동이2');

-- 에러가 발생할 경우 rollback이 실행됨
rollback;
-- commit 전까지는 rollback을 통해 쿼리들이 undo될 수 있음
insert into
	orders (customer_name)
values
	('동길이2');

commit;

select
	*
from
	orders
where
	customer_name = '길동이2';

# 인덱스(index)
# 풀스캔 - 처음부터 끝까지 모두 스캔(데이터가 쌓이면 느려진다)
# 인덱스는 데이터의 색인을 만들어주는 기능
# B+-tree 구조를 가지고 있어서 어떤 값을 찾을때 좀 더 빠르게 찾을 수 있음
# primary key 컬럼은 자동으로 index가 만들어진다
# uique제약도 마찮가지로 자동으로 index가 만들어진다

# create index 인덱스이름 on 테이블(컬럼명)
create index index_comments_post_id on comments (post_id);

-- 만들어진 인덱스 목록 조회
show index from comments;

-- 인덱스 삭제
-- drop index 인덱스이름 on 테이블이름
-- drop index index_comments_post_id on comments

# 일반적으로 index를 어디에다가 걸어야 할까?
-- 1. where절에 자주쓰이는 컬럼
-- 2. join할때 on절에 자주쓰이는 컬럼
-- 3. order by에 자주쓰이는 컬럼

# 인덱스를 못타는 경우
-- 1. like연산시 '%라떼%' -> 앞쪽에 %가 있으면 인덱스가 무효
-- '아이스%' -> 인덱스 가능
-- 2. 컬럼에 함수를 적용하면 인덱스가 무효
-- year(create_at) = 2025 -> 2025년에 해당하는 모든 컬럼
-- create_at >= '2025-01-01' and create_at < '2026-01-01'

# 실행계획을 조회해보기
-- 쿼리 앞에 explain을 붙히면 실행계획 조회 가능
explain select * from comments;
/*
	실행계획의 type
    ALL -> 풀스캔
    index -> 인덱스 스캔
    ref(eq_ref) -> 인덱스를 비교연산해서 사용
    const -> 상수
*/

explain select * from comments where post_id = 3;









