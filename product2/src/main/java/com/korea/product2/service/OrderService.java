package com.korea.product2.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.korea.product2.DTO.OrderDTO;
import com.korea.product2.DTO.ProductDTO;
import com.korea.product2.model.OrderEntity;
import com.korea.product2.model.ProductEntity;
import com.korea.product2.repository.OrderRepository;
import com.korea.product2.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository O_Repository;
	
	private final ProductRepository P_Repository;
	
	//주문 내역 조회하기 
	
	public List<OrderDTO> findOrder(){
		List<Object[]> results = O_Repository.findAllOrderTotalPrices();
		return OrderDTO.toListOrderDTO(results);
	}
	
	//주문하기 
	public List<ProductDTO> save(OrderDTO dto){
		Optional<ProductEntity> option = P_Repository.findById(dto.getProductId());
		ProductEntity productEntity;
		//상품이 조회된다면
		if(option.isPresent()) {
			productEntity = option.get(); //option 객체에서 데이터를 꺼내온다.
		}else {
			throw new RuntimeException("없는 상품");
		}
		
		//재고 확인
		if(productEntity.getProductCount()<dto.getProductCount()) {
			throw new RuntimeException("재고 부족. 현재 재고 : "+productEntity.getProductCount());
		}
		
		OrderEntity order = OrderEntity.builder()
										.product(productEntity)
										.productCount(dto.getProductCount())
										.build();
		//주문 내역 저장
		O_Repository.save(order);
		
		//재고 감소
		productEntity.setProductCount(productEntity.getProductCount()-dto.getProductCount());
		
		//db에 수정된 재고 업데이트
		P_Repository.save(productEntity);
		
		return P_Repository
					.findAll()
					.stream()
					.map(entity-> new ProductDTO(entity))
					.collect(Collectors.toList());
		
	}
}
