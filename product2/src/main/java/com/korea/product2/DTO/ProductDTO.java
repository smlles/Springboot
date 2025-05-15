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
	int productPrice;
	LocalDateTime creationTime;
	LocalDateTime updateTime;
	
	
	
	public ProductDTO(ProductEntity entity) {
		
		this.productId = entity.getProductId();
		this.productName = entity.getProductName();
		this.productCount = entity.getProductCount();
		this.productPrice = entity.getProductPrice();
		this.creationTime = entity.getCreationTime();
		this.updateTime = entity.getUpdateTime();
	}
	
	public static ProductEntity toEntity(ProductDTO dto) {
		return ProductEntity.builder()
				.productId(dto.getProductId())
				.productName(dto.getProductName())
				.productCount(dto.getProductCount())
				.productPrice(dto.getProductPrice())
				.creationTime(dto.getCreationTime())
				.updateTime(dto.getUpdateTime())
				.build();
	}
	
	
}
