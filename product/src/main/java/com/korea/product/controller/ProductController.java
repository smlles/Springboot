package com.korea.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.korea.product.DTO.ProductDTO;
import com.korea.product.model.ProductEntity;
import com.korea.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private final ProductService service; 
	
	
	//추가를 해야한다 -> @PostMapping을 갖는 메서드
	//사용자가 인터페이스에서 데이터를 주겠다 -> 매개변수로 받는다.
	//JSON으로 넘어오는걸 자바 객체로 받아야한다 -> @RequsetBody
	//데이터를 받는것은 DTO를 써야한다. ->ProductDTO
	
	//추가
	@PostMapping		//컨트롤러의 반환값은 항상ResponseEntity<?>, 매개변수는 항상 DTO
	public ResponseEntity<?> addProduct(@RequestBody ProductDTO dto){
		//매개변수로 온 데이터를 서비스로 넘겨야한다. 
		//추가하고 전체 조회를 하니까 ProductDTO타입의 리스트에 담아야한다.
		List<ProductDTO> response = service.addProduct(dto);
		//갔다 왔으니까, 반환 받자
		return ResponseEntity.ok(response);
			}
	
	//조회 
	//그런데 최소 금액을 전달 할 수도 있다.
	@GetMapping
	public ResponseEntity<?> getAllProduct(
			@RequestParam(required=false) Double minPrice){
		
		List<ProductDTO> products = service.getFilteredProduct(minPrice);
		return ResponseEntity.ok(products);
	}
	
	
	
	
	//id로 수정
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable int id,@RequestBody ProductDTO dto){
		
		List<ProductDTO> updateProduct = service.updateProduct(id,dto);
		return ResponseEntity.ok(updateProduct);
	}
	
	//삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable int id){
		
		boolean isDeleted = service.deleteProduct(id);
		if(isDeleted) {//삭제되긴 했는데, 딱히 보여줄건 없음 204 No Content 응답
			return ResponseEntity.noContent().build();
		}//삭제할 녀석을 애초에 못 찾은 404Not Found응답
		return ResponseEntity.notFound().build();
	}
	
}
