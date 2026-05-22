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
