package com.edutalk.edutalk.controller;

import com.edutalk.edutalk.dto.ChatRoomDto;
import com.edutalk.edutalk.model.ChatMessage;
import com.edutalk.edutalk.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.edutalk.edutalk.config.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	// 새로운 채팅방 생성 또는 기존 채팅방 조회
	@PostMapping("/room")
	public ResponseEntity<ChatRoomDto> createOrGetChatRoom(@RequestParam String identy,
			@RequestParam String instructorId, @RequestParam String instructorName, @RequestParam String studentId,
			@RequestParam String studentName) {
		try {
			ChatRoomDto chatRoom = chatService.getOrCreateChatRoom(identy, instructorId, instructorName, studentId,
					studentName);
			return ResponseEntity.status(HttpStatus.CREATED).body(chatRoom);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 모든 채팅방 목록 조회
	@GetMapping("/rooms")
	public ResponseEntity<List<ChatRoomDto>> getAllChatRooms() {
		List<ChatRoomDto> chatRooms = chatService.getAllChatRooms();
		return ResponseEntity.ok(chatRooms);
	}

	// 특정 채팅방의 메시지 조회
	@GetMapping("/messages") // 경로를 /messages로 변경하고 @PathVariable 대신 @RequestParam 사용
	public ResponseEntity<?> getChatMessages(@RequestParam("chatId") String chatRoomUidString, // chatId를 쿼리 파라미터로 받음
			@AuthenticationPrincipal CustomUserDetails currentUser) {

		if (currentUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new java.util.HashMap<String, Object>() {
				{
					put("success", false);
					put("error", new java.util.HashMap<String, String>() {
						{
							put("message", "인증이 필요합니다. 로그인 후 다시 시도해주세요.");
							put("code", "UNAUTHORIZED");
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

		try {
			UUID chatRoomUid = UUID.fromString(chatRoomUidString);
			List<ChatMessage> messages = chatService.getChatMessages(chatRoomUid);

			// Remix 프론트엔드의 응답 형식에 맞게 조정
			// { success: true, data: { messages: [...] }, meta: {...} } 형태를 기대합니다.
			return ResponseEntity.ok(new java.util.HashMap<String, Object>() {
				{
					put("success", true);
					put("data", new java.util.HashMap<String, Object>() {
						{
							put("messages", messages);
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
			// UUID 형식이 잘못된 경우
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new java.util.HashMap<String, Object>() {
				{
					put("success", false);
					put("error", new java.util.HashMap<String, String>() {
						{
							put("message", "유효하지 않은 채팅방 ID 형식입니다.");
							put("code", "INVALID_CHAT_ID_FORMAT");
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
		} catch (Exception e) {
			// 기타 오류 처리
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new java.util.HashMap<String, Object>() {
						{
							put("success", false);
							put("error", new java.util.HashMap<String, String>() {
								{
									put("message", "채팅 메시지 조회 중 오류가 발생했습니다.");
									put("code", "CHAT_MESSAGE_FETCH_ERROR");
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

	// 메시지 전송 및 저장
	@PostMapping("/message/{chatRoomUid}")
	public ResponseEntity<ChatMessage> sendMessage(@PathVariable UUID chatRoomUid, @RequestParam String senderId,
			@RequestParam String senderName, @RequestBody String messageContent) { // 메시지 내용은 RequestBody로 받음
		try {
			ChatMessage savedMessage = chatService.saveMessage(chatRoomUid, senderId, senderName, messageContent);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

    // Remix 프론트엔드의 /api/edit/chat 엔드포인트 구현
    // 메시지 전송, 파일 업로드, 채팅 삭제 등 다양한 작업을 처리
    @PostMapping("/edit/chat")
    public ResponseEntity<?> handleChatEdit(
            @RequestBody String requestBody, // 요청 본문을 String으로 먼저 받아서 파싱
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (currentUser == null) {
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

        try {
            // 요청 본문을 JSON 객체로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(requestBody);

            String intent = jsonNode.has("intent") ? jsonNode.get("intent").asText() : null;
            String sub = jsonNode.has("sub") ? jsonNode.get("sub").asText() : null; // api.dashboard.jsx에서 사용

            // intent 또는 sub 값에 따라 작업 분기
            if ("message".equals(sub) || "send".equals(sub)) { // 메시지 전송
                String messageContent = jsonNode.has("message") ? jsonNode.get("message").asText() : null;
                String chatIdString = jsonNode.has("chatId") ? jsonNode.get("chatId").asText() : null;
                boolean encrypted = jsonNode.has("encrypted") && jsonNode.get("encrypted").asBoolean();

                if (messageContent == null || chatIdString == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new java.util.HashMap<String, Object>() {{
                            put("success", false);
                            put("error", new java.util.HashMap<String, String>() {{
                                put("message", "메시지 전송에 필요한 필드가 누락되었습니다.");
                                put("code", "MISSING_MESSAGE_FIELDS");
                            }});
                            put("meta", new java.util.HashMap<String, String>() {{
                                put("timestamp", java.time.Instant.now().toString());
                            }});
                        }}
                    );
                }

                UUID chatRoomUid = UUID.fromString(chatIdString);
                // ChatService의 saveMessage 메서드를 재활용하거나 새로운 메서드 추가
                ChatMessage savedMessage = chatService.saveMessage(
                    chatRoomUid,
                    currentUser.getUid().toString(), // senderId
                    currentUser.getName(), // senderName
                    messageContent
                );

                return ResponseEntity.ok(
                    new java.util.HashMap<String, Object>() {{
                        put("success", true);
                        put("data", new java.util.HashMap<String, Object>() {{
                            put("message", "메시지가 성공적으로 전송되었습니다.");
                            put("savedMessage", savedMessage);
                        }});
                        put("meta", new java.util.HashMap<String, String>() {{
                            put("timestamp", java.time.Instant.now().toString());
                        }});
                    }}
                );
            } else if ("drop".equals(sub)) { // 채팅 삭제
                String chatIdString = jsonNode.has("id") ? jsonNode.get("id").asText() : null; // api.dashboard.jsx에서 'id' 사용

                if (chatIdString == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new java.util.HashMap<String, Object>() {{
                            put("success", false);
                            put("error", new java.util.HashMap<String, String>() {{
                                put("message", "채팅 삭제에 필요한 ID가 누락되었습니다.");
                                put("code", "MISSING_CHAT_ID");
                            }});
                            put("meta", new java.util.HashMap<String, String>() {{
                                put("timestamp", java.time.Instant.now().toString());
                            }});
                        }}
                    );
                }

                UUID chatRoomUid = UUID.fromString(chatIdString);
                chatService.deleteChatRoom(chatRoomUid); 

                return ResponseEntity.ok(
                    new java.util.HashMap<String, Object>() {{
                        put("success", true);
                        put("data", new java.util.HashMap<String, Object>() {{
                            put("message", "채팅방이 성공적으로 삭제되었습니다.");
                        }});
                        put("meta", new java.util.HashMap<String, String>() {{
                            put("timestamp", java.time.Instant.now().toString());
                        }});
                    }}
                );
            } else if ("file".equals(sub)) { // 파일 업로드 (MultipartFile 처리 필요)
                // 이 부분은 @RequestBody 대신 @RequestPart 또는 @RequestParam으로 MultipartFile을 받아야 합니다.
                // 현재 @RequestBody String requestBody로는 파일 데이터를 직접 처리할 수 없습니다.
                // 별도의 메서드 또는 @PostMapping 어노테이션을 사용하여 MultipartFile을 처리해야 합니다.
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new java.util.HashMap<String, Object>() {{
                        put("success", false);
                        put("error", new java.util.HashMap<String, String>() {{
                            put("message", "파일 업로드는 별도의 엔드포인트 또는 MultipartFile 처리가 필요합니다.");
                            put("code", "FILE_UPLOAD_NOT_SUPPORTED_HERE");
                        }});
                        put("meta", new java.util.HashMap<String, String>() {{
                            put("timestamp", java.time.Instant.now().toString());
                        }});
                    }}
                );
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new java.util.HashMap<String, Object>() {{
                        put("success", false);
                        put("error", new java.util.HashMap<String, String>() {{
                            put("message", "알 수 없는 작업 요청입니다.");
                            put("code", "UNKNOWN_INTENT");
                        }});
                        put("meta", new java.util.HashMap<String, String>() {{
                            put("timestamp", java.time.Instant.now().toString());
                        }});
                    }}
                );
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new java.util.HashMap<String, Object>() {{
                    put("success", false);
                    put("error", new java.util.HashMap<String, String>() {{
                        put("message", "유효하지 않은 ID 형식입니다.");
                        put("code", "INVALID_ID_FORMAT");
                        put("details", e.getMessage());
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
                        put("message", "채팅 편집 중 오류가 발생했습니다.");
                        put("code", "CHAT_EDIT_ERROR");
                        put("details", e.getMessage());
                    }});
                    put("meta", new java.util.HashMap<String, String>() {{
                        put("timestamp", java.time.Instant.now().toString());
                    }});
                }}
            );
        }
    }
    
    @PostMapping("/upload/file") // 새로운 파일 업로드 엔드포인트
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("chatId") String chatIdString,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (currentUser == null) {
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

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new java.util.HashMap<String, Object>() {{
                    put("success", false);
                    put("error", new java.util.HashMap<String, String>() {{
                        put("message", "업로드할 파일이 없습니다.");
                        put("code", "NO_FILE_UPLOADED");
                    }});
                    put("meta", new java.util.HashMap<String, String>() {{
                        put("timestamp", java.time.Instant.now().toString());
                    }});
                }}
            );
        }

        try {
            UUID chatRoomUid = UUID.fromString(chatIdString);
            // ChatService를 통해 파일 저장 및 메시지 전송 로직 호출
            // 이 메서드는 ChatService에 새로 추가해야 합니다.
            ChatMessage savedMessage = chatService.saveFileMessage(
                chatRoomUid,
                currentUser.getUid().toString(),
                currentUser.getName(),
                file
            );

            return ResponseEntity.ok(
                new java.util.HashMap<String, Object>() {{
                    put("success", true);
                    put("data", new java.util.HashMap<String, Object>() {{
                        put("message", "파일이 성공적으로 업로드되고 메시지로 전송되었습니다.");
                        put("savedMessage", savedMessage);
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
                        put("message", "유효하지 않은 채팅방 ID 형식입니다.");
                        put("code", "INVALID_CHAT_ID_FORMAT");
                        put("details", e.getMessage());
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
                        put("message", "파일 업로드 중 오류가 발생했습니다.");
                        put("code", "FILE_UPLOAD_ERROR");
                        put("details", e.getMessage());
                    }});
                    put("meta", new java.util.HashMap<String, String>() {{
                        put("timestamp", java.time.Instant.now().toString());
                    }});
                }}
            );
        }
    }

    @PostMapping // /api/chat 에 대한 POST 요청 처리
    public ResponseEntity<?> createChatRoom(
            @RequestBody String requestBody,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        if (currentUser == null) {
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

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(requestBody);

            String toId = jsonNode.has("to") ? jsonNode.get("to").asText() : null;
            String toName = jsonNode.has("to_name") ? jsonNode.get("to_name").asText() : null;

            if (toId == null || toName == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new java.util.HashMap<String, Object>() {{
                        put("success", false);
                        put("error", new java.util.HashMap<String, String>() {{
                            put("message", "채팅방 생성에 필요한 'to' 또는 'to_name' 필드가 누락되었습니다.");
                            put("code", "MISSING_CHAT_ROOM_FIELDS");
                        }});
                        put("meta", new java.util.HashMap<String, String>() {{
                            put("timestamp", java.time.Instant.now().toString());
                        }});
                    }}
                );
            }

            // ChatService를 통해 채팅방 생성 또는 조회
            ChatRoomDto chatRoom = chatService.getOrCreateChatRoom(
                currentUser.getIdenty(),
                currentUser.getUserType().equals("teacher") ? currentUser.getUid().toString() : toId, // 강사 ID
                currentUser.getUserType().equals("teacher") ? currentUser.getName() : toName,         // 강사 이름
                currentUser.getUserType().equals("student") ? currentUser.getUid().toString() : toId, // 학생 ID
                currentUser.getUserType().equals("student") ? currentUser.getName() : toName          // 학생 이름
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(
                new java.util.HashMap<String, Object>() {{
                    put("success", true);
                    put("data", new java.util.HashMap<String, Object>() {{
                        put("message", "채팅방이 성공적으로 생성 또는 조회되었습니다.");
                        put("chat", chatRoom);
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
                        put("message", "채팅방 생성 중 오류가 발생했습니다.");
                        put("code", "CHAT_ROOM_CREATION_ERROR");
                        put("details", e.getMessage());
                    }});
                    put("meta", new java.util.HashMap<String, String>() {{
                        put("timestamp", java.time.Instant.now().toString());
                    }});
                }}
            );
        }
    }

    
	@GetMapping
	public ResponseEntity<?> getChatList(@AuthenticationPrincipal CustomUserDetails currentUser) {
		if (currentUser == null) {
			// 인증되지 않은 사용자 처리 (Spring Security 설정에 따라 달라질 수 있음)
			// Remix 프론트엔드의 ErrorResponses.UNAUTHORIZED()와 유사하게 응답
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new java.util.HashMap<String, Object>() {
				{
					put("success", false);
					put("error", new java.util.HashMap<String, String>() {
						{
							put("message", "인증이 필요합니다. 로그인 후 다시 시도해주세요.");
							put("code", "UNAUTHORIZED");
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

		String identy = currentUser.getIdenty();
		String userType = currentUser.getUserType(); // CustomUserDetails에서 가져온 사용자 타입
		String userId = currentUser.getUid().toString(); // CustomUserDetails에서 가져온 사용자 UID

		List<ChatRoomDto> chatRooms = chatService.getChatListForUser(identy, userType, userId);

		// Remix 프론트엔드의 응답 형식에 맞게 조정
		// api.chat.jsx의 loader 함수는 { chats: [...], user: {...} } 형태를 기대합니다.
		return ResponseEntity.ok(new java.util.HashMap<String, Object>() {
			{
				put("success", true); // 성공 응답
				put("data", new java.util.HashMap<String, Object>() {
					{
						put("chats", chatRooms);
						put("user", new java.util.HashMap<String, String>() {
							{
								put("id", currentUser.getUid().toString());
								put("type", currentUser.getUserType());
								put("identy", currentUser.getIdenty());
								put("name", currentUser.getName());
							}
						});
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