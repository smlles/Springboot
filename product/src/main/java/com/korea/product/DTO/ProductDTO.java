package com.korea.product.DTO;

import com.korea.product.model.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDTO {
	int id;
	String name;
	String description;
	int price;
	
	public ProductDTO(ProductEntity entity) {
		
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
	}
	
	public static ProductEntity toEntity(ProductDTO dto) {
		return ProductEntity.builder()
					.id(dto.getId())
					.name(dto.getName())
					.description(dto.getDescription())
					.price(dto.getPrice())
					.build();
	}
}
