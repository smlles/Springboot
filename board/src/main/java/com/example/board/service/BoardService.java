package com.example.board.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.board.DTO.BoardDTO;
import com.example.board.model.BoardEntity;
import com.example.board.repository.BoardRespository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRespository repository;

	
	//그냥 보기
	public List<BoardEntity> findBoardList() {
		return repository.findAll(Sort.by(Sort.Direction.DESC, "writingTime"));
	}
	//그냥 쓰기
	public List<BoardEntity> writePost(BoardDTO dto) {
		BoardEntity entity = dto.toEntity(dto);
		repository.save(entity);
		return findBoardList();
	}
	//id로 하나 보기
	public Optional<BoardEntity> findById(Long id) {
		return repository.findById(id);
		
//		public List<BoardDTO> findById(LongId){
//		Optional<BoardEntity> option = repository.findById(id);
//		List<BoardDTO> = null;
//		있으면 꺼내기
//		if(option.isPresent()) {
//		list = option.stream()
//				.map(BoardDTO::fromEmtity
//				.collect(Collectors.toList());
//		}
//		return list;
		
	}
	//id 로 삭제하기
	public boolean deletePost(long id) {
		if(repository.existsById(id)) {
		repository.deleteById(id);
		return true;
		}else {
		return false;
		}
	}
	//id로 하나 수정하기
	public BoardDTO updatePost(Long id, BoardDTO boardDTO) {
//		BoardEntity entity = repository.findById(id).orElseThrow(() -> new RuntimeException("없는 게시물"));
		//기존의 내용 꺼내기
		BoardEntity entity = repository.findById(id).get();
//		수정한 내용 반영
		entity.setTitle(boardDTO.getTitle());
		entity.setAuthor(boardDTO.getAuthor());
		entity.setContent(boardDTO.getContent());
		entity.setWritingTime(boardDTO.getWritingTime());

		return BoardDTO.fromEntity(repository.save(entity));
	}
}
