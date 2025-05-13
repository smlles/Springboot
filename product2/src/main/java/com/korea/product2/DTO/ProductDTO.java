package com.korea.product2.DTO;

import java.time.LocalDateTime;

import com.korea.product2.model.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDTO {
	int productId; 
	String productName;
	int productCount;
	int price;
	LocalDateTime creationTime;
	LocalDateTime updateTime;
	
	
	
	public ProductDTO(ProductEntity entity) {
		
		this.productId = entity.getProductId();
		this.productName = entity.getProductName();
		this.productCount = entity.getProductCount();
		this.price = entity.getPrice();
		this.creationTime = entity.getCreationTime();
		this.updateTime = entity.getUpdateTime();
	}
	
	public static ProductEntity toEntity(ProductDTO dto) {
		return ProductEntity.builder()
				.productId(dto.getProductId())
				.productName(dto.productName)
				.productCount(dto.getProductCount())
				.price(dto.getPrice())
				.creationTime(dto.getCreationTime())
				.updateTime(dto.getUpdateTime())
				.build();
	}
	
	
}
