package com.example.user2.DTO;

import com.example.user2.model.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	
	private String id;
	private String password;
	private String address;
	private String email;
	
	
	public static UserDTO fromEntity(UserEntity entity) {
		return UserDTO.builder()
				.id(entity.getId())
				.password(entity.getPassword())
				.address(entity.getAddress())
				.email(entity.getEmail())
				.build();
	}
	public static UserEntity toEntity(UserDTO dto) {
		return UserEntity.builder()
				.id(dto.getId())
				.password(dto.getPassword())
				.address(dto.getAddress())
				.email(dto.getEmail())
				.build();
	}
}



	
	
	

