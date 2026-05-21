# day3_join
/*
	정규화하는 이유
    1. 갱신이상 - 상품가격 업데이트시 상품 출현하는 모든 행 업데이트
    2. 삽입이상 - 주문되지 않은 상품은 기록조차 되지 못한다
	3. 삭제이상 - 주문을 지웠더니, 손님 정보가 아예 사라질 수 있음
    
    정규화 단계
    1정규화 - 한셀에는 값 하나만
    2정규화 - pk가 여러 컬럼일때, 부분적으로만 결정되는 컬럼이 없도록
    3정규화 - 다른컬럼을 거쳐서 결정되는 컬럼이 없도록
    
    
    -> 결국에는 정보 취합을 해야한다(join)
    -> 취합을 할때 연결고리(외래키-fk)가 필요함
*/

# innerjoin
# 양쪽에 모두 있는 데이터를 합쳐준다(null을 제외하겠다)

select
	*
from orders o
inner join order_details od on o.order_id = od.order_id;

-- product_id만 있고, 상품명 x

select
	o.order_id,
    o.customer_name,
    o.ordered_at,
    p.product_name,
    p.price,
    od.quantity
from orders o 
inner join order_details od on o.order_id = od.order_id
inner join product p on od.product_id = p.product_id;

select * from comments;

-- 게시글 + 그에 달린 댓글을 모두 보여주세요
-- post와 comments inner join을 해주세요
-- 결과컬럼: post_id, title, author, body
-- post_id 오름차순, 같다면 comment_id 오름차순

select
	p.post_id,
	p.title,
    c.author,
    c.body
from post p
inner join comments c on p.post_id = c.post_id
order by
p.post_id, comment_id;
