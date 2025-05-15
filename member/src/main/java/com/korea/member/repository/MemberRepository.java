package com.korea.member.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.korea.member.model.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Integer>{

	Optional<MemberEntity> findByEmail(String email);
}
