package com.example.board.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.board.DTO.BoardDTO;
import com.example.board.DTO.ResponseDTO;
import com.example.board.model.BoardEntity;
import com.example.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

		private final BoardService service;
		
		//전체 보기
		@GetMapping
		public ResponseEntity<?> findBoardList(){
			
			List<BoardEntity> list= service.findBoardList();
			List<BoardDTO> dtos = list.stream().map(BoardDTO::fromEntity).collect(Collectors.toList());
			
			ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().data(dtos).build();
			return ResponseEntity.ok(response);
		}
		//id로 게시글 찾기
		@GetMapping("/{id}")
		public ResponseEntity<?> findByIdBoard(@PathVariable("id") Long id ){
			Optional<BoardEntity> entityOpt = service.findById(id);
//		    List<BoardDTO> list= service findByIdBoard(id)
//			responseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().data(list).build();
//			return ResonseEntity.ok().body(response);
		    if(entityOpt.isEmpty()) {
		        // 게시글이 없으면 404 응답
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아니 게시물이 없다니까?");
		    }

		    BoardDTO dto = BoardDTO.fromEntity(entityOpt.get());
		    return ResponseEntity.ok(dto);
		}
		

		//글 쓰기
		@PostMapping
		public ResponseEntity<?> writePost(@RequestBody BoardDTO dto){
			List<BoardEntity> list = service.writePost(dto);
			List<BoardDTO> dtos = list.stream().map(BoardDTO::fromEntity).collect(Collectors.toList());
			
			ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().data(dtos).build();
			return ResponseEntity.ok(response);
		}
		//id로 글 삭제
		@DeleteMapping("/{id}")
		public ResponseEntity<?> deletePost(@PathVariable("id") Long id){
			boolean result= service.deletePost(id);
//			List<BoardDTO> dtos = list.stream().map(BoardDTO::fromEntity).collect(Collectors.toList());
//			
//			ResponseDTO<BoardDTO> response = ResponseDTO.<BoardDTO>builder().data(dtos).build();
			return ResponseEntity.ok(result);
		}
		//id로 글 수정
		@PutMapping("/{id}")
		public ResponseEntity<?> updatePost(@PathVariable("id") Long id, @RequestBody BoardDTO boardDTO) {
		    
		    return ResponseEntity.ok(service.updatePost(id, boardDTO));
		}
}
