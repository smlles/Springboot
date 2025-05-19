package com.example.user2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user2.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

}
