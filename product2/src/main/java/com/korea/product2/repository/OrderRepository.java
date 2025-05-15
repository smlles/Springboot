package com.korea.product2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.korea.product2.model.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer>{
	//모든 주문 내역 조회 쿼리만들기
	//총 결재 내역 조회하기
	//@manyToOne으로 설정된 관계매핑 덕분에 orderEntity에서 product필드를 통해
	//productEntity에 접근 할 수 있다.
	//JPA는 JPQL쿼리를 분석하고, orderEntity에서 ProductEntity와의 관계가
	//설정된 것을 감지하면 자동으로 SQL조인을 생성
	//아래 쿼리에서 o.product를 통해 자동으로 productEntity와의 조인이 이루어지기 떄문에
	//JOIN을 쓰지 않아도 JPA는 적절한 SQL 조인쿼리를 생성한다.
	

    @Query("SELECT o.orderId, o.product.productName, o.productCount, o.product.productPrice, (o.productCount * o.product.productPrice) AS totalPrice " +
           "FROM OrderEntity o")
    List<Object[]> findAllOrderTotalPrices();

	//제네릭이 object 배열인 이유 => JPQL쿼리에서 여러개의 값을 선택(SELECT)하면
	//한 행에 대응하는 결과가 OBJECT 배열로 볂 ㅘㄴ되어 반환되기 떄문
	//o.orderId-> Object[0]
}
