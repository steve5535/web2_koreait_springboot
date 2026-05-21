# 집계함수
-- 여러 행 -> 한 숫자로
select
	count(price) as rowCount, -- count(컬럼명 or *)
    sum(price) as priceSum,-- sum(컬럼명)
    avg(price) as priceAvg, -- avg(컬럼명)
    min(price) as minPrice, -- min(컬럼명)
    max(price) as maxPrice -- max(컬럼명)
from
	product;

-- null을 허용하는 컬럼은 count() 시 주의
-- count(*)은 null도 카운팅한다
-- count(컬럼)은 null 카운팅 하지 않는다

# round - 소수를 다룰 때
select
	round(avg(price)) as 정수, -- 정수로 반올림
    round(avg(price), 2) as 소수한자리, -- 두번째 매개변수가 양수면 소수자리 표현
    round(avg(price), -2) as 백원단위 -- 두번째 매개변수가 음수면 정수자리표현
from
	product;

# ifnull(컬럼, 대체할 값) -- null이라면 대체할 값으로 치환
select
	ifnull(content, '비어있음') as content
from
	post;

# 날짜함수
select now(); -- 현재시간
insert into
	post(title, content, create_at)
values
	('sk하이닉스 다시 떡상', null, now());

/*
date_format(날짜, 포맷 표현) : 원하는 모양으로 출력하고 싶을때
%Y 4자리 연도 %y 2자리 연도
%m 월(01~12) %c 월(1~12)
%d 일(01~31) %e 일(1~31)
$H 시(24시기준)
%i 분
%s 초
*/
select
	product_name,
    date_format(create_at, '%Y년 %m월 %d일') as create_at
from
	product;

# concat() -- 문자열 이어 붙히기
select
	concat(product_name, '-', price, '원') as 메뉴판
from
	product;

# case-when - if-elseif-else의 sql버전
-- 위에서 아래로 평가되며, 처음 true가 곳에서 실행하고 탈출
select
	product_name,
    price,
    case -- if문 시작
		when price < 2500 then '저렴' -- 조건
        when price < 5000 then '보통' -- 조건
        else '비쌈' -- 그외
	end as 가격대 -- if문 종료
from
	product;

-- 1) post테이블에서 가장오래된 글의 작성시간 그리고 가장 최근글의 작성시간 출력
select
	max(create_at) as 최신글,
    min(create_at) as 오래된글
from
	post;
-- 2) post테이블에서 모든 row를 '제목 - 본문'으로 하나의 값으로 표현
-- null일 경우, '(본문없음)' 으로 대체
select
	concat(title, '-', ifnull(content, '(본문없음)')) as 표현식
from
	post;
-- 3) post테이블에서 제복, 작성시각, 시간대(오전, 오후, 밤)
select
	title,
    create_at,
    case
		when date_format(create_at, '%H') <= 12 then '오전'
        when date_format(create_at, '%H') <= 18 then '오후'
        else '밤'
	end as 시간대
from
	post
order by
	create_at asc;

# group by
-- 중복값을 하나의 값으로 압축
-- 같은 가격을 가진 상품이 몇개지?(집계함수와 같이 쓰인다)
select
	price,
	count(*) as 상품수
from
	product
group by
	price;

-- 월별 등록 상품갯수
select
	date_format(create_at, '%Y-%m') as 월,
	count(*) as 상품갯수
from
	product
group by
	date_format(create_at, '%Y-%m')
order by
	월; -- order by는 select보다 뒤쪽 순서

# having - 그룹 결과에 다시 조건을 걸기위해 존재
-- where -> 그룹화 전, 원래 행에 조건
-- having -> 그룹화 루, 묶음 결과에 조건
select
	price,
	count(*) as 상품수
from
	product
group by
	price
having
	count(*) >= 2;
-- count(*)는 그룹화 후에 나오는 값이라 where에선 쓸 수 없음
/*
1. from -- 테이블 & 조인 수행
2. where -- 행 필터링
3. group by - 그룹화
4. having -- 그룹 필터링
5. select -- 컬럼 필터링, as(별칭)이 메모리에 생성
6. order by -- 정렬
7. limit offset -- 몇 개?
*/

-- product 테이블에서
-- 가격대별 가격 합계 및 평균가
-- 정렬: 평균가 비싼 순
-- 가격대분류: 저렴( < 2500), 보통( < 4000), 비쌈(그외)
select
	case
		when price < 2500 then '저렴' -- 조건문
        when price < 4000 then '보통'
		else '비쌈'
	end as 가격대,
	count(*) as 상품수
from
	product
group by
	case
		when price < 2500 then '저렴'
		when price < 4000 then '보통'
		else '비쌈'
	end
having
	count(*) >= 2
order by
	가격대 desc;

