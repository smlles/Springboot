package com.korea.member.DTO;

import com.korea.member.model.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
	
	private int id;
	private String name;
	@Column(nullable=false,unique=true)
	private String email;
	private String password;
	
	
	//E->D
	//밑에처럼 해도 됨
	
	//public static MemberDTO fromEntity(MemberEntity member){
	//return DTO.builder~}
	public MemberDTO(MemberEntity entity) {
		
		this.id = entity.getId();
		this.name = entity.getName();
		this.email = entity.getEmail();
		this.password = entity.getPassword();
	}
	
	//D->E
	public static MemberEntity toEntity(MemberDTO dto) {
		
		return MemberEntity.builder()
							.id(dto.getId())
							.name(dto.getName())
							.email(dto.getEmail())
							.password(dto.getPassword())
							.build();
			
	}
	
	
}
