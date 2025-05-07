package com.korea.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//클라이언트와 주고 받을 때(요청, 응답) DTO에 담아서 준다
//데이터가 계층간 이동 할 때 (controller ->service->repository)
//Entity에 담아서 옮긴다.
 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
	private int id;
	private String name;
	private String email;
	
	//Entity -> DTO 변환
	public UserDTO(UserEntity entity) {
		
		this.id = entity.getId();
		this.name = entity.getName();
		this.email = entity.getEmail();
	}
	
	//DTO -> Entity
	public static UserEntity toEntity(UserDTO dto) {
		return UserEntity.builder()
				.id(dto.getId())
				.name(dto.getName())
				.email(dto.getEmail())
				.build();
	}
}
