package com.example.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.board.model.BoardEntity;

@Repository
public interface BoardRespository extends JpaRepository<BoardEntity, Long>{

}
