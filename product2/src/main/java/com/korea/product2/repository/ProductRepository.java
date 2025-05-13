package com.korea.product2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.korea.product2.model.ProductEntity;

@Repository //얘도 bean으로 올라감
public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{

		
}
