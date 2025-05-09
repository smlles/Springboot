package com.korea.todo.dto;

import com.korea.todo.model.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
	private String token;
	private String username;
	private String password;
	private String id;
	
//	
//	public UserDTO(UserEntity entity) {
//		
//		this.token = token;
//		this.username = getUsername();
//		this.password = getPassword();
//		this.id = getId();
//	}
	
	
	
}



