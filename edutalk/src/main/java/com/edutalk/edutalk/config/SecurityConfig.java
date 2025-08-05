package com.edutalk.edutalk.config;

import com.edutalk.edutalk.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.servlet.Filter;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		// Remix 개발 서버 주소 및 Vite 기본 포트 추가
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						// Public endpoints (no authentication required)
						.requestMatchers("/api/auth/register").permitAll() // 회원가입
						.requestMatchers("/api/auth/login").permitAll() // 로그인
						.requestMatchers("/api/docs/**").permitAll() // API 문서 (가정)
						.requestMatchers("/auth/**").permitAll() // 이메일 인증, 비밀번호 재설정 등 (Remix의 auth.* 경로)
						.requestMatchers("/api/generate-keys").permitAll() // 키 생성 (인증 전에도 가능하도록)
						.requestMatchers("/api/generate-token").permitAll() // 토큰 생성 (인증 전에도 가능하도록)
						.requestMatchers("/api/chat/rooms").permitAll() // 모든 채팅방 목록 조회 (인증 없이 가능하도록)

						// All other /api/** requests require authentication
						.requestMatchers("/api/**").authenticated()

						// Other paths that might need specific handling
						.requestMatchers("/dashboard/**").authenticated() // 대시보드 페이지 (Remix의 dashboard.* 경로)

						// All other requests require authentication
						.anyRequest().authenticated());

		// JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 이전에 추가
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
