package com.edutalk.edutalk.config;

import com.edutalk.edutalk.model.ContractorEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

	private ContractorEntity contractor;

	public CustomUserDetails(ContractorEntity contractor) {
		this.contractor = contractor;
	}

	public UUID getUid() {
		return contractor.getUid();
	}

	public String getIdenty() {
		return contractor.getIdenty();
	}

	public String getEmail() {
		return contractor.getEmail();
	}

	public String getUserType() {
		// ContractorEntity에 type 필드가 있으므로, 해당 값을 반환
		return contractor.getType();
	}

	public String getName() {
		// ContractorEntity에 name 필드가 있으므로, 해당 값을 반환
		return contractor.getName();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 사용자 역할에 따라 권한 부여 (예: ROLE_TEACHER, ROLE_STUDENT)
		// 현재는 간단하게 "ROLE_USER"를 반환합니다.
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return contractor.getPassword();
	}

	@Override
	public String getUsername() {
		return contractor.getUid().toString(); // 사용자 UID를 username으로 사용
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return contractor.isCertified(); // 이메일 인증 여부를 계정 활성화 여부로 사용
	}
}
