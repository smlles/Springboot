package com.korea.product2.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name="Product")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int productId; 
	String productName;
	int productCount;
	int price;
	@CreationTimestamp //insert쿼리가 발생하면 현재 시간 값 적용
	LocalDateTime creationTime;
	@UpdateTimestamp //update쿼리가 발생하면 현재 시간 값 적용
	LocalDateTime updateTime;
}
