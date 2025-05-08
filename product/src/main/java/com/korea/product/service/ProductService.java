package com.korea.product.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.korea.product.DTO.ProductDTO;
import com.korea.product.model.ProductEntity;
import com.korea.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {

	
	@Autowired
	private final ProductRepository repository;
	
	//받은 데이터를 DB에 넣고, 전체 조회해서 반환해야하니까
	//반환형은 List<ProductDTO>여야한다.
	//상품 넣기							서비스의 매개변수는, 어디서 DTO ->Entity를 하느냐 차이
	public List<ProductDTO> addProduct(ProductDTO dto){
		//jpa메서드를 이용해 DB에 추가
		ProductEntity entity = ProductDTO.toEntity(dto);
		//jpa로 DB에 CRUD 할 떄 엔티티로 해야함
		//save()에 들어가는게 entity다.
		//넘어온 DTO를 Entity로 받아야한다.
		repository.save(entity);
		
		//전체 조회값을 반환하려면 findAll()
		//findAll()의 반환형은 List<ProductEntity> 이므로 List<ProductDTO>로 바뀌어야한다.
		return repository.findAll().stream().map(ProductDTO::new).collect(Collectors.toList());
	}
	
	//상품 조회 (최소 금액이 있다면 그 이상만 조회)
	public List<ProductDTO> getFilteredProduct(Double minPrice){
		List<ProductEntity> products = repository.findAll();
		//가격 필터링 (minPrice가 있다면)
		if(minPrice!=null) {
			products = products
					.stream()
					.filter(product->product.getPrice()>=minPrice)//지랄...
					.collect(Collectors.toList());
		}
		return products.stream().map(ProductDTO::new).collect(Collectors.toList());
	}
	

	
	//id가지고 상품 수정
	public List<ProductDTO> updateProduct(int id,ProductDTO dto) {
		//형태가 findbyid는 optional
		Optional<ProductEntity> productOptional = repository.findById(id);
		//데이터가 있어?
		if(productOptional.isPresent()) {
			//Optional에 있는 데이터를 꺼낸다.
			ProductEntity entity = productOptional.get();
			//수정하려는 데이터로 다시 세팅
			entity.setName(dto.getName());
			entity.setDescription(dto.getDescription());
			entity.setPrice(dto.getPrice());
			
			repository.save(entity);
		}
		
		return repository.findAll().stream().map(ProductDTO::new).collect(Collectors.toList());
	}
	
	
	//상품 삭제
	public boolean deleteProduct(int id){
	//id를 통해 DB에 데이터가있는지 확인
		Optional<ProductEntity> optionalEntity = repository.findById(id);
		if(optionalEntity.isPresent()) {
			//DB에서 삭제
			repository.deleteById(id);
			return true;
		}
		return false;
	}
}
