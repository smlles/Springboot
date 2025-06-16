package com.korea.rnBoard.DTO;

import com.korea.rnBoard.domain.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //게터세터
@NoArgsConstructor //기본 생성자
@AllArgsConstructor //모든 필드를 매개변수로 갖는 생성자
@Builder
public class DTO {
	
	private int id;
	private String title;
	private String author;
	private String description;
	private String time;
	private int views;
	
	
	//Entity -> DTO
	public DTO(Entity entity) {
		
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.author = entity.getAuthor();
		this.description = entity.getDescription();
		this.time = entity.getTime();
		this.views = entity.getViews();
	}
	
	//DTO -> ENtity
	static public Entity ToEntity(DTO dto) {
		
		return Entity.builder()
				.id(dto.getId())
				.title(dto.getTitle())
				.author(dto.getAuthor())
				.description(dto.getDescription())
				.time(dto.getTime())
				.views(dto.getViews())
				.build();
		
	}
	
	
}
