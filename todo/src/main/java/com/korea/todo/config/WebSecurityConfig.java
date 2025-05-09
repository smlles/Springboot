package com.korea.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

import com.korea.todo.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity//스프링 시큐리티 필터 체인, 설정 활성화
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	//필터 클래스 주입
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	@Bean //빈으로 등록
	protected DefaultSecurityFilterChain securityFilterChain(
			HttpSecurity http) throws Exception{
		
	}
	
	
	
	
}
