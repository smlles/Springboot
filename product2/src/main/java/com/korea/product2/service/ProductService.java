package com.korea.product2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.korea.product2.DTO.ProductDTO;
import com.korea.product2.DTO.ResponseDTO;
import com.korea.product2.model.ProductEntity;
import com.korea.product2.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	//레파지토리 주입
	private final ProductRepository repository;
	
	
	public List<ProductDTO> create(ProductDTO dto){
		//엔티티 전환
		ProductEntity entity = ProductDTO.toEntity(dto);
		//엔티티가 유효한지 검사
		validate(entity);
		//저장하기 JPA에 데이터를 전달 할 때에는 Entity타입이어야 한다.
		repository.save(entity);
		//DB에 추가하고 전체 정보 반환
		return repository.findAll().stream().map(ProductDTO::new).collect(Collectors.toList());
	}
	
	
	
	public List<ProductEntity> findAll(){
		return repository.findAll();
	}
	
	private void validate(ProductEntity entity) {
		if(entity==null) {
			throw new RuntimeException("Entity cannot be null");
		}
	}
	
	
}
