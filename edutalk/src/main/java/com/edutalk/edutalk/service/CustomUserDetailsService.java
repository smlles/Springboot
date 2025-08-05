package com.edutalk.edutalk.service;

import com.edutalk.edutalk.model.ContractorEntity;
import com.edutalk.edutalk.repository.ContractorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.edutalk.edutalk.config.CustomUserDetails;

import java.util.Collections;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private ContractorRepository contractorRepository;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		// userId는 JWT의 'sub' 클레임이며, 여기서는 ContractorEntity의 uid로 가정합니다.
		// Remix 프론트엔드의 JWT payload를 확인하여 'sub'에 어떤 값이 들어가는지 확인해야 합니다.
		// 현재 Remix의 JWT payload는 'id' 필드를 'sub'로 사용하고 있습니다.
		// 따라서 여기서는 'id' (UUID 문자열)를 사용하여 ContractorEntity를 찾습니다.

		UUID userUuid;
		try {
			userUuid = UUID.fromString(userId);
		} catch (IllegalArgumentException e) {
			throw new UsernameNotFoundException("Invalid user ID format: " + userId);
		}

		ContractorEntity contractor = contractorRepository.findById(userUuid)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

		// CustomUserDetails 객체 반환
		return new CustomUserDetails(contractor);
	}
}
