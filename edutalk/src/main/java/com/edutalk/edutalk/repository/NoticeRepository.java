package com.edutalk.edutalk.repository;


  import com.edutalk.edutalk.model.NoticeEntity;
  import org.springframework.data.jpa.repository.JpaRepository;
  import org.springframework.stereotype.Repository;

  import java.util.UUID;


  @Repository
  public interface NoticeRepository extends JpaRepository<NoticeEntity, UUID> {
      // 현재는 기본적인 CRUD 기능만 필요하므로 추가적인 메소드는 정의하지 않습니다.
      // 추후 특정 조건으로 공지사항을 검색해야 할 경우, 여기에 쿼리 메소드를 추가할 수 있습니다.
      // 예: List<NoticeEntity> findByDeleteIsFalseOrderByCreateTimeDesc();
  }