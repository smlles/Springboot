package com.korea.product2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korea.product2.DTO.OrderDTO;
import com.korea.product2.DTO.ProductDTO;
import com.korea.product2.DTO.ResponseDTO;
import com.korea.product2.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
	
	private final OrderService O_Service;
	
	//주문 조회 기능
	
	@GetMapping("/total")
	public ResponseEntity<?> findOrder(){
		//db에서 전체 조회해서 반환
		List<OrderDTO> list = O_Service.findOrder();
		ResponseDTO<OrderDTO> response = ResponseDTO.<OrderDTO>builder().data(list).build();		
		
		return ResponseEntity.ok(response);
	}
	
	
	
	@PostMapping
	public ResponseEntity<?> saveOrder(@RequestBody OrderDTO dto){
		List<ProductDTO> list = O_Service.save(dto);
		ResponseDTO<ProductDTO> response = ResponseDTO.<ProductDTO>builder().data(list).build();
		return ResponseEntity.ok(response);
	}
	
}
