package com.korea.rnBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.korea.rnBoard.domain.Entity;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Entity,Integer>{

	
}
