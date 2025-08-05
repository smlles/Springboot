package com.edutalk.edutalk.controller;

import com.edutalk.edutalk.dto.LoginRequestDto;
import com.edutalk.edutalk.dto.RegisterRequestDto;
import com.edutalk.edutalk.dto.TokenDto;
import com.edutalk.edutalk.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference; // Map<String, Object> 변환을 위해 필요

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/auth/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequestDto requestDto) {
		try {
			authService.register(requestDto);
			// 성공 응답을 Remix 프론트엔드 형식에 맞게 변경
			return ResponseEntity.status(HttpStatus.CREATED).body(new java.util.HashMap<String, Object>() {
				{
					put("success", true);
					put("data", new java.util.HashMap<String, String>() {
						{
							put("message", "회원가입이 성공적으로 완료되었습니다. 이메일 인증을 확인해주세요.");
							put("email", requestDto.getEmail()); // 등록된 이메일 반환
						}
					});
					put("meta", new java.util.HashMap<String, String>() {
						{
							put("timestamp", java.time.Instant.now().toString());
						}
					});
				}
			});
		} catch (IllegalArgumentException e) {
			// BAD_REQUEST 오류 응답을 Remix 프론트엔드 형식에 맞게 변경
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new java.util.HashMap<String, Object>() {
				{
					put("success", false);
					put("error", new java.util.HashMap<String, String>() {
						{
							put("message", e.getMessage());
							put("code", "REGISTRATION_FAILED");
						}
					});
					put("meta", new java.util.HashMap<String, String>() {
						{
							put("timestamp", java.time.Instant.now().toString());
						}
					});
				}
			});
		} catch (Exception e) {
			// INTERNAL_SERVER_ERROR 오류 응답을 Remix 프론트엔드 형식에 맞게 변경
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new java.util.HashMap<String, Object>() {
						{
							put("success", false);
							put("error", new java.util.HashMap<String, String>() {
								{
									put("message", "회원가입 중 오류가 발생했습니다.");
									put("code", "INTERNAL_SERVER_ERROR");
									put("details", e.getMessage());
								}
							});
							put("meta", new java.util.HashMap<String, String>() {
								{
									put("timestamp", java.time.Instant.now().toString());
								}
							});
						}
					});
		}
	}

	@PostMapping("/auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto) {
	    try {
	        // AuthService의 login 메서드가 TokenDto를 반환한다고 가정
	        TokenDto tokenDto = authService.login(requestDto);

	        // 성공 응답을 Remix 프론트엔드 형식에 맞게 변경
	        return ResponseEntity.ok(
	            new java.util.HashMap<String, Object>() {{
	                put("success", true);
	                put("data", new java.util.HashMap<String, Object>() {{
	                    put("token", tokenDto.getAccessToken()); // Access Token
	                    // Remix 프론트엔드에서 payload를 직접 사용하므로, 필요하다면 여기에 추가
	                    // 예: put("payload", tokenDto.getPayload());
	                }});
	                put("meta", new java.util.HashMap<String, String>() {{
	                    put("timestamp", java.time.Instant.now().toString());
	                }});
	            }}
	        );
	    } catch (IllegalArgumentException e) {
	        // UNAUTHORIZED 오류 응답을 Remix 프론트엔드 형식에 맞게 변경
	        // auth.login.jsx에서 needsVerification 필드를 확인하므로, 이를 포함
	        boolean needsVerification = e.getMessage() != null && e.getMessage().contains("이메일 인증이 필요합니다"); // 임시 로직, AuthService에서 명확히 반환하도록 수정 필요
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
	            new java.util.HashMap<String, Object>() {{
	                put("success", false);
	                put("error", new java.util.HashMap<String, Object>() {{
	                    put("message", e.getMessage());
	                    put("code", "AUTHENTICATION_FAILED");
	                    put("needsVerification", needsVerification);
	                }});
	                put("meta", new java.util.HashMap<String, String>() {{
	                    put("timestamp", java.time.Instant.now().toString());
	                }});
	            }}
	        );
	    } catch (Exception e) {
	        // INTERNAL_SERVER_ERROR 오류 응답을 Remix 프론트엔드 형식에 맞게 변경
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
	            new java.util.HashMap<String, Object>() {{
	                put("success", false);
	                put("error", new java.util.HashMap<String, String>() {{
	                    put("message", "로그인 중 오류가 발생했습니다.");
	                    put("code", "INTERNAL_SERVER_ERROR");
	                    put("details", e.getMessage());
	                }});
	                put("meta", new java.util.HashMap<String, String>() {{
	                    put("timestamp", java.time.Instant.now().toString());
	                }});
	            }}
	        );
	    }
	}


	@PostMapping("/generate-keys")
	public ResponseEntity<?> generateKeys() {
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
	                new java.util.HashMap<String, Object>() {{
	                    put("success", false);
	                    put("error", new java.util.HashMap<String, String>() {{
	                        put("message", "인증이 필요합니다. 로그인 후 다시 시도해주세요.");
	                        put("code", "UNAUTHORIZED");
	                    }});
	                    put("meta", new java.util.HashMap<String, String>() {{
	                        put("timestamp", java.time.Instant.now().toString());
	                    }});
	                }}
	            );
	        }

	        String userIdString = authentication.getName();
	        UUID userId = UUID.fromString(userIdString);

	        Map<String, String> keyPair = authService.generateAndSaveKeys(userId);

	        // 성공 응답을 Remix 프론트엔드 형식에 맞게 변경
	        return ResponseEntity.ok(
	            new java.util.HashMap<String, Object>() {{
	                put("success", true);
	                put("data", new java.util.HashMap<String, Object>() {{
	                    put("message", "API 키가 성공적으로 생성되었습니다.");
	                    put("publicKey", keyPair.get("publicKey"));
	                    put("privateKey", keyPair.get("privateKey"));
	                }});
	                put("meta", new java.util.HashMap<String, String>() {{
	                    put("timestamp", java.time.Instant.now().toString());
	                }});
	            }}
	        );
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	            new java.util.HashMap<String, Object>() {{
	                put("success", false);
	                put("error", new java.util.HashMap<String, String>() {{
	                    put("message", e.getMessage());
	                    put("code", "KEY_GENERATION_FAILED");
	                }});
	                put("meta", new java.util.HashMap<String, String>() {{
	                    put("timestamp", java.time.Instant.now().toString());
	                }});
	            }}
	        );
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
	            new java.util.HashMap<String, Object>() {{
	                put("success", false);
	                put("error", new java.util.HashMap<String, String>() {{
	                    put("message", "API 키 생성 중 오류가 발생했습니다.");
	                    put("code", "INTERNAL_SERVER_ERROR");
	                    put("details", e.getMessage());
	                }});
	                put("meta", new java.util.HashMap<String, String>() {{
	                    put("timestamp", java.time.Instant.now().toString());
	                }});
	            }}
	        );
	    }
	}


	@PostMapping("/generate-token")
	public ResponseEntity<?> generateToken(
	        @RequestParam("tokenType") String tokenType,
	        @RequestParam("payload") String payloadJson) {
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
	                new HashMap<String, Object>() {{
	                    put("success", false);
	                    put("error", new HashMap<String, String>() {{
	                        put("message", "인증이 필요합니다. 로그인 후 다시 시도해주세요.");
	                        put("code", "UNAUTHORIZED");
	                    }});
	                    put("meta", new HashMap<String, String>() {{
	                        put("timestamp", java.time.Instant.now().toString());
	                    }});
	                }}
	            );
	        }

	        String userIdString = authentication.getName();
	        UUID userId = UUID.fromString(userIdString);

	        ObjectMapper objectMapper = new ObjectMapper();
	        Map<String, Object> payloadMap = objectMapper.readValue(payloadJson, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});

	        Map<String, Object> tokenInfo = authService.generateJwtToken(userId, tokenType, payloadMap);

	        return ResponseEntity.ok(
	            new HashMap<String, Object>() {{
	                put("success", true);
	                put("data", new HashMap<String, Object>() {{
	                    put("token", tokenInfo.get("token"));
	                    put("payload", tokenInfo.get("payload"));
	                    put("tokenType", tokenInfo.get("tokenType"));
	                }});
	                put("meta", new HashMap<String, String>() {{
	                    put("timestamp", java.time.Instant.now().toString());
	                }});
	            }}
	        );
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
	            new HashMap<String, Object>() {{
	                put("success", false);
	                put("error", new HashMap<String, String>() {{
	                    put("message", e.getMessage());
	                    put("code", "TOKEN_GENERATION_FAILED");
	                }});
	                put("meta", new HashMap<String, String>() {{
	                    put("timestamp", java.time.Instant.now().toString());
	                }});
	            }}
	        );
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
	            new HashMap<String, Object>() {{
	                put("success", false);
	                put("error", new HashMap<String, String>() {{
	                    put("message", "JWT 토큰 생성 중 오류가 발생했습니다.");
	                    put("code", "INTERNAL_SERVER_ERROR");
	                    put("details", e.getMessage());
	                }});
	                put("meta", new HashMap<String, String>() {{
	                    put("timestamp", java.time.Instant.now().toString());
	                }});
	            }}
	        );
	    }
	}

	@GetMapping("/auth/reset-contractors")
	public ResponseEntity<?> resetContractors() {
		authService.resetContractors();
		return ResponseEntity.ok("Contractor table has been reset.");
	}
}
