package com.edutalk.edutalk.controller;

import com.edutalk.edutalk.config.CustomUserDetails;
import com.edutalk.edutalk.dto.CreateNoticeRequestDto;
import com.edutalk.edutalk.dto.NoticeDto;
import com.edutalk.edutalk.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeService noticeService;

	// 새로운 공지사항 생성
	@PostMapping
	public ResponseEntity<NoticeDto> createNotice(@RequestBody CreateNoticeRequestDto requestDto) {
		try {
			NoticeDto newNotice = noticeService.createNotice(requestDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(newNotice);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * 공지사항 생성, 수정, 삭제 요청을 처리하는 엔드포인트. Remix 프론트엔드의 /dashboard/notices (POST) 요청을
	 * 처리합니다.
	 * 
	 * @param intent      작업 의도 (create, update, delete)
	 * @param uid         공지사항 UID (수정, 삭제 시 필요)
	 * @param title       공지사항 제목 (생성, 수정 시 필요)
	 * @param context     공지사항 내용 (생성, 수정 시 필요)
	 * @param instructor  작성자 (생성 시 필요)
	 * @param currentUser 현재 인증된 사용자 정보
	 * @return 작업 결과
	 */
	@PostMapping("/edit") // /api/notice/edit 엔드포인트
	public ResponseEntity<?> handleNoticeEdit(@RequestParam String intent, @RequestParam(required = false) String uid, // UUID
																														// 문자열
			@RequestParam(required = false) String title, @RequestParam(required = false) String context,
			@RequestParam(required = false) String instructor, @AuthenticationPrincipal CustomUserDetails currentUser) {

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
			switch (intent) {
			case "create":
				if (title == null || context == null || instructor == null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new java.util.HashMap<String, Object>() {
						{
							put("success", false);
							put("error", new java.util.HashMap<String, String>() {
								{
									put("message", "공지사항 생성에 필요한 필드가 누락되었습니다.");
									put("code", "MISSING_NOTICE_FIELDS");
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
				CreateNoticeRequestDto createRequest = new CreateNoticeRequestDto();
				createRequest.setTitle(title);
				createRequest.setContext(context);
				createRequest.setInstructor(instructor);
				NoticeDto createdNotice = noticeService.createNotice(createRequest);
				final String createMessage = "공지사항이 성공적으로 생성되었습니다.";
				return ResponseEntity.ok(new java.util.HashMap<String, Object>() {
					{
						put("success", true);
						put("data", new java.util.HashMap<String, Object>() {
							{
								put("message", createMessage);
								put("notice", createdNotice);
							}
						});
						put("meta", new java.util.HashMap<String, String>() {
							{
								put("timestamp", java.time.Instant.now().toString());
							}
						});
					}
				});
			case "update":
				if (uid == null || title == null || context == null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new java.util.HashMap<String, Object>() {
						{
							put("success", false);
							put("error", new java.util.HashMap<String, String>() {
								{
									put("message", "공지사항 수정에 필요한 필드가 누락되었습니다.");
									put("code", "MISSING_NOTICE_FIELDS");
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
				CreateNoticeRequestDto updateRequest = new CreateNoticeRequestDto();
				updateRequest.setTitle(title);
				updateRequest.setContext(context);
				NoticeDto updatedNotice = noticeService.updateNotice(UUID.fromString(uid), updateRequest);
				final String updateMessage = "공지사항이 성공적으로 수정되었습니다.";
				return ResponseEntity.ok(new java.util.HashMap<String, Object>() {
					{
						put("success", true);
						put("data", new java.util.HashMap<String, Object>() {
							{
								put("message", updateMessage);
								put("notice", updatedNotice);
							}
						});
						put("meta", new java.util.HashMap<String, String>() {
							{
								put("timestamp", java.time.Instant.now().toString());
							}
						});
					}
				});
			case "delete":
				if (uid == null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new java.util.HashMap<String, Object>() {
						{
							put("success", false);
							put("error", new java.util.HashMap<String, String>() {
								{
									put("message", "공지사항 삭제에 필요한 UID가 누락되었습니다.");
									put("code", "MISSING_NOTICE_UID");
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
				noticeService.deleteNotice(UUID.fromString(uid));
				final String deleteMessage = "공지사항이 성공적으로 삭제되었습니다.";
				return ResponseEntity.ok(new java.util.HashMap<String, Object>() {
					{
						put("success", true);
						put("data", new java.util.HashMap<String, Object>() {
							{
								put("message", deleteMessage);
							}
						});
						put("meta", new java.util.HashMap<String, String>() {
							{
								put("timestamp", java.time.Instant.now().toString());
							}
						});
					}
				});
			default:
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new java.util.HashMap<String, Object>() {
					{
						put("success", false);
						put("error", new java.util.HashMap<String, String>() {
							{
								put("message", "알 수 없는 작업 의도입니다.");
								put("code", "UNKNOWN_INTENT");
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

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new java.util.HashMap<String, Object>() {
				{
					put("success", false);
					put("error", new java.util.HashMap<String, String>() {
						{
							put("message", "유효하지 않은 UID 형식입니다.");
							put("code", "INVALID_UID_FORMAT");
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new java.util.HashMap<String, Object>() {
						{
							put("success", false);
							put("error", new java.util.HashMap<String, String>() {
								{
									put("message", "공지사항 처리 중 오류가 발생했습니다.");
									put("code", "NOTICE_PROCESSING_ERROR");
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

	@GetMapping("/search") // /api/search 엔드포인트 (클래스 레벨 @RequestMapping("/api")와 결합)
	public ResponseEntity<?> searchNotices(@RequestParam("sub") String sub,
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

		if (!"notice".equals(sub)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new java.util.HashMap<String, Object>() {
				{
					put("success", false);
					put("error", new java.util.HashMap<String, String>() {
						{
							put("message", "지원하지 않는 검색 타입입니다.");
							put("code", "UNSUPPORTED_SEARCH_TYPE");
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
			List<NoticeDto> notices = noticeService.getAllNotices(); // 모든 공지사항을 가져옴

			return ResponseEntity.ok(new java.util.HashMap<String, Object>() {
				{
					put("success", true);
					put("data", new java.util.HashMap<String, Object>() {
						{
							put("notices", notices);
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new java.util.HashMap<String, Object>() {
						{
							put("success", false);
							put("error", new java.util.HashMap<String, String>() {
								{
									put("message", "공지사항 검색 중 오류가 발생했습니다.");
									put("code", "NOTICE_SEARCH_ERROR");
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

	// 모든 공지사항 조회
	@GetMapping
	public ResponseEntity<List<NoticeDto>> getAllNotices() {
		List<NoticeDto> notices = noticeService.getAllNotices();
		return ResponseEntity.ok(notices);
	}

	// 특정 공지사항 조회
	@GetMapping("/{uid}")
	public ResponseEntity<NoticeDto> getNoticeById(@PathVariable UUID uid) {
		try {
			NoticeDto notice = noticeService.getNoticeById(uid);
			return ResponseEntity.ok(notice);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 공지사항 수정
	@PutMapping("/{uid}")
	public ResponseEntity<NoticeDto> updateNotice(@PathVariable UUID uid,
			@RequestBody CreateNoticeRequestDto requestDto) {
		try {
			NoticeDto updatedNotice = noticeService.updateNotice(uid, requestDto);
			return ResponseEntity.ok(updatedNotice);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 공지사항 삭제 (소프트 삭제)
	@DeleteMapping("/{uid}")
	public ResponseEntity<Void> deleteNotice(@PathVariable UUID uid) {
		try {
			noticeService.deleteNotice(uid);
			return ResponseEntity.noContent().build(); // 204 No Content
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}