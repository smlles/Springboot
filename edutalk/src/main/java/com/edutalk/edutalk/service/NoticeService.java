package com.edutalk.edutalk.service;


  import com.edutalk.edutalk.dto.CreateNoticeRequestDto;
  import com.edutalk.edutalk.dto.NoticeDto;
  import com.edutalk.edutalk.model.NoticeEntity;
  import com.edutalk.edutalk.repository.NoticeRepository;
  import lombok.RequiredArgsConstructor;
  import org.springframework.stereotype.Service;
  import org.springframework.transaction.annotation.Transactional;


  import java.time.LocalDateTime;
  import java.util.List;
  import java.util.UUID;
  import java.util.stream.Collectors;

  @Service
  @RequiredArgsConstructor
  public class NoticeService {

      private final NoticeRepository noticeRepository;


      // 새로운 공지사항 생성
      @Transactional
      public NoticeDto createNotice(CreateNoticeRequestDto requestDto) {
          NoticeEntity noticeEntity = new NoticeEntity();
          noticeEntity.setTitle(requestDto.getTitle());
          noticeEntity.setContext(requestDto.getContext());
          noticeEntity.setInstructor(requestDto.getInstructor()); // 작성자 정보
          noticeEntity.setIdenty("default"); // TODO: 실제 사용자 identy로 변경 필요
          noticeEntity.setCreateTime(LocalDateTime.now()); // @CreationTimestamp가 자동으로 처리
          noticeEntity.setUpdateTime(LocalDateTime.now()); // @UpdateTimestamp가 자동으로 처리  
          noticeEntity.setDisplayOrder(0); // 기본값
          noticeEntity.setDelete(false); // 기본값

          NoticeEntity savedNotice = noticeRepository.save(noticeEntity);
          return convertToNoticeDto(savedNotice);
      }


      // 모든 공지사항 조회 (삭제되지 않은 것만)
      @Transactional(readOnly = true)
      public List<NoticeDto> getAllNotices() {
          // TODO: 실제로는 delete=false 이고 displayOrder, createTime 기준으로 정렬해야 합니다.
          // 예: noticeRepository.findByDeleteIsFalseOrderByDisplayOrderAscCreateTimeDesc();    
          List<NoticeEntity> notices = noticeRepository.findAll();
          return notices.stream()
                  .filter(notice -> !notice.isDelete()) // 삭제되지 않은 공지사항만 필터링      
                  .map(this::convertToNoticeDto)
                  .collect(Collectors.toList());
      }


      // 특정 공지사항 조회
      @Transactional(readOnly = true)
      public NoticeDto getNoticeById(UUID uid) {
          NoticeEntity noticeEntity = noticeRepository.findById(uid)
                  .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + uid));
          return convertToNoticeDto(noticeEntity);
      }


      // 공지사항 수정
      @Transactional
      public NoticeDto updateNotice(UUID uid, CreateNoticeRequestDto requestDto) {
          NoticeEntity noticeEntity = noticeRepository.findById(uid)
                  .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + uid));


          noticeEntity.setTitle(requestDto.getTitle());
          noticeEntity.setContext(requestDto.getContext());
          noticeEntity.setInstructor(requestDto.getInstructor());
          noticeEntity.setUpdateTime(LocalDateTime.now()); // @UpdateTimestamp가 자동으로 처리

          NoticeEntity updatedNotice = noticeRepository.save(noticeEntity);
          return convertToNoticeDto(updatedNotice);
      }


      // 공지사항 삭제 (소프트 삭제)
      @Transactional
      public void deleteNotice(UUID uid) {
          NoticeEntity noticeEntity = noticeRepository.findById(uid)
                  .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + uid));


          noticeEntity.setDelete(true); // 실제 삭제 대신 delete 플래그를 true로 설정
          noticeRepository.save(noticeEntity);
      }


      // NoticeEntity를 NoticeDto로 변환하는 헬퍼 메소드
      private NoticeDto convertToNoticeDto(NoticeEntity noticeEntity) {
          NoticeDto dto = new NoticeDto();
          dto.setId(noticeEntity.getUid());
          dto.setTitle(noticeEntity.getTitle());
          dto.setContent(noticeEntity.getContext());
          dto.setAuthor(noticeEntity.getInstructor());
          dto.setCreatedAt(noticeEntity.getCreateTime());
          dto.setDisplayOrder(noticeEntity.getDisplayOrder());
          return dto;
      }
  }