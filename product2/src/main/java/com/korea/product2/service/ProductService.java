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
		//저장하기
		repository.save(entity);
		//반환
		return repository.findAll().stream().map(ProductDTO::new).collect(Collectors.toList());
	}
	
	
	
	public List<ProductEntity> findAll(){
		
		
		
		return repository.findAll();
	}
}
