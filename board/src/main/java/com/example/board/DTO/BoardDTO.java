package com.example.board.DTO;

import org.springframework.data.annotation.CreatedDate;

import com.example.board.model.BoardEntity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
	
	private Long id;
	private String title;
	private String author;
	@CreatedDate
	private String writingTime;
	private String content;
	
	public static BoardDTO fromEntity(BoardEntity entity) {
		return BoardDTO.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.author(entity.getAuthor())
				.writingTime(entity.getWritingTime())
				.content(entity.getContent())
				.build();
	}
	
	public static BoardEntity toEntity(BoardDTO dto) {
		
		return BoardEntity.builder()
					.id(dto.getId())
					.title(dto.getTitle())
					.author(dto.getAuthor())
					.writingTime(dto.getWritingTime())
					.content(dto.getContent())
					.build();
	}
	
}
