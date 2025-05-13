package com.korea.product2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korea.product2.DTO.ProductDTO;
import com.korea.product2.model.ProductEntity;
import com.korea.product2.service.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/product") //공통 주소 제공
@RestController
public class ProductController {

	private final ProductService service;
	
	
	//만들기
	@PostMapping
	public ResponseEntity<?> create(@RequestBody ProductDTO dto) {
		//엔티티로 보내기
		List<ProductDTO> response = service.create(dto);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping
	public ResponseEntity<?> findAll(@RequestBody ProductDTO dto){
		//db에서 전체 조회해서 반환
		List<ProductDTO> response = service.findAll();
		
		return ResponseEntity.ok(response);
	}
	
}
