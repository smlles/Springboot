package com.edutalk.edutalk.service;

import com.edutalk.edutalk.dto.LoginRequestDto;
import com.edutalk.edutalk.dto.RegisterRequestDto;
import com.edutalk.edutalk.dto.TokenDto;
import com.edutalk.edutalk.model.ContractorEntity;
import com.edutalk.edutalk.repository.ContractorRepository;
import com.edutalk.edutalk.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final ContractorRepository contractorRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Transactional
	public void register(RegisterRequestDto requestDto) {
		if (contractorRepository.findByEmail(requestDto.getEmail()).isPresent()) {
			throw new IllegalArgumentException("이미 등록된 이메일입니다.");
		}
		if (contractorRepository.findByIdenty(requestDto.getIdenty()).isPresent()) {
			throw new IllegalArgumentException("이미 사용 중인 계약사명입니다.");
		}

		String hashedPassword = passwordEncoder.encode(requestDto.getPassword());
		
		String passwordSalt = UUID.randomUUID().toString();  // 랜덤한 salt 생성
		
		ContractorEntity newContractor = new ContractorEntity();
		newContractor.setIdenty(requestDto.getIdenty());
		newContractor.setEmail(requestDto.getEmail());
		newContractor.setPassword(hashedPassword);
		newContractor.setPasswordSalt(passwordSalt); // Generate a unique salt
		newContractor.setCertified(false);

		contractorRepository.save(newContractor);
	}

	@Transactional
	public void resetContractors() {
		contractorRepository.deleteAll();
	}

	@Transactional
	public TokenDto login(LoginRequestDto requestDto) {
		// 1. 사용자 이메일로 ContractorEntity 조회
		ContractorEntity contractor = contractorRepository.findByEmail(requestDto.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요."));

		// 2. 비밀번호 검증
		if (!passwordEncoder.matches(requestDto.getPassword(), contractor.getPassword())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호를 확인해주세요.");
		}

		// 3. 이메일 인증 여부 확인
		if (!contractor.isCertified()) {
			// 이메일 인증이 필요한 경우, 특정 메시지를 포함한 예외 발생
			throw new IllegalArgumentException("이메일 인증이 필요합니다. 이메일을 확인해주세요.");
		}

		// 4. Spring Security Authentication 객체 생성 (토큰 발급용)
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				contractor.getUid().toString(), requestDto.getPassword());

		// 5. JWT 토큰 생성 ('list' 타입의 토큰 생성)
		Map<String, Object> loginPayload = new HashMap<String, Object>() {
			{
				put("identy", contractor.getIdenty());
				put("type", contractor.getType()); // ContractorEntity의 type 필드 사용
				put("sub", "list");
				put("id", contractor.getUid().toString());
				put("name", contractor.getName()); // ContractorEntity의 name 필드 사용
				put("from", contractor.getUid().toString());
				put("to", "");
				put("from_name", contractor.getName()); // ContractorEntity의 name 필드 사용
				put("to_name", "");
			}
		};

		Map<String, Object> tokenInfo = generateJwtToken(contractor.getUid(), "list", // 토큰 타입
				loginPayload // 직접 Map을 전달
		);

		// TokenDto 생성 및 반환
		TokenDto tokenDto = new TokenDto();
		tokenDto.setAccessToken((String) tokenInfo.get("token"));
		// tokenDto.setRefreshToken((String) tokenInfo.get("refreshToken")); // Refresh
		// Token이 있다면 사용

		return tokenDto;
	}

	@Transactional
	public Map<String, String> generateAndSaveKeys(UUID userId) {
		Optional<ContractorEntity> optionalContractor = contractorRepository.findById(userId);
		if (optionalContractor.isEmpty()) {
			throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
		}
		ContractorEntity contractor = optionalContractor.get();

		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(2048);
			KeyPair pair = keyGen.generateKeyPair();
			PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();

			String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n"
					+ Base64.getEncoder().encodeToString(privateKey.getEncoded()) + "\n-----END PRIVATE KEY-----";
			String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n"
					+ Base64.getEncoder().encodeToString(publicKey.getEncoded()) + "\n-----END PUBLIC KEY-----";

			contractor.setPrivateKey(privateKeyPem);
			contractor.setPublicKey(publicKeyPem);
			contractor.setPeriodTime(LocalDateTime.now());
			contractor.setExpirationTime(LocalDateTime.now().plusYears(1));

			contractorRepository.save(contractor);

			Map<String, String> response = new HashMap<>();
			response.put("message", "API 키가 성공적으로 생성되었습니다.");
			response.put("privateKey", privateKeyPem);
			response.put("publicKey", publicKeyPem);
			response.put("userId", userId.toString());
			response.put("identy", contractor.getIdenty());

			return response;

		} catch (Exception e) {
			throw new RuntimeException("RSA 키 생성 및 저장 중 오류 발생: " + e.getMessage(), e);
		}
	}

	@Transactional
	public Map<String, Object> generateJwtToken(UUID userId, String tokenType, Map<String, Object> payloadMap) {
		Optional<ContractorEntity> optionalContractor = contractorRepository.findById(userId);
		if (optionalContractor.isEmpty()) {
			throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
		}
		ContractorEntity contractor = optionalContractor.get();
//
//		if (contractor.getPrivateKey() == null) {
//			throw new IllegalArgumentException("RSA 키 쌍이 없습니다. /api/key 페이지에서 키를 생성해주세요.");
//		}

		try {
			String identy = (String) payloadMap.get("identy");
			String type = (String) payloadMap.get("type");
			String sub = (String) payloadMap.get("sub");
			String id = (String) payloadMap.get("id");
			String name = (String) payloadMap.get("name");
			System.out.println(payloadMap);
//			여기서 캐치구문으로 들어감
			String token = jwtTokenProvider.generateToken(identy, type, sub, id, name, null);
		
			Map<String, Object> response = new HashMap<>();
			System.out.println("2트--------------------------------------------------------------");
			System.out.println(response);
			response.put("token", token);
			response.put("payload", payloadMap);
			response.put("tokenType", tokenType);
			
			return response;

		} catch (Exception e) {
			throw new RuntimeException("JWT 토큰 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}

}
