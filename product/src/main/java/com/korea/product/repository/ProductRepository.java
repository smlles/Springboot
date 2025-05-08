package com.korea.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.korea.product.model.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
	
	
	ProductEntity findByPrice(int price);

}
