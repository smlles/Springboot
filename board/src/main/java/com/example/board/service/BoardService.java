package com.example.board.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.board.DTO.BoardDTO;
import com.example.board.model.BoardEntity;
import com.example.board.repository.BoardRespository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRespository repository;

	public List<BoardEntity> findBoardList() {
		return repository.findAll();
	}

	public List<BoardEntity> writePost(BoardDTO dto) {
		BoardEntity entity = dto.toEntity(dto);
		repository.save(entity);
		return repository.findAll();
	}

	public Optional<BoardEntity> findById(Long id) {
		return repository.findById(id);
//		Optional<BoardEntity> option = repository.findById(id);
//		BoardEntity entity = null;
//		if(option.isPresent()) {
//		entity=	option.get();
//		}
//		return Arrays.asList(entity);
	}

	public List<BoardEntity> deletePost(long id) {
		repository.deleteById(id);
		return repository.findAll();
	}

	public BoardDTO updatePost(Long id, BoardDTO boardDTO) {
		BoardEntity entity = repository.findById(id).orElseThrow(() -> new RuntimeException("게시글 없음"));

		entity.setTitle(boardDTO.getTitle());
		entity.setAuthor(boardDTO.getAuthor());
		entity.setContent(boardDTO.getContent());
		entity.setWritingTime(boardDTO.getWritingTime());

		repository.save(entity);

		return BoardDTO.fromEntity(entity);
	}
}
