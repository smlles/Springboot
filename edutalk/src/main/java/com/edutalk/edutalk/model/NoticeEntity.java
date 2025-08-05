package com.edutalk.edutalk.model;


  import jakarta.persistence.*;
  import lombok.Getter;
  import lombok.Setter;
  import org.hibernate.annotations.CreationTimestamp;
  import org.hibernate.annotations.GenericGenerator; 
  import org.hibernate.annotations.UpdateTimestamp;  

  import java.time.LocalDateTime;
  import java.util.UUID;

  @Entity
  @Table(name = "notice")
  @Getter
  @Setter
  public class NoticeEntity {


      @Id
      @GeneratedValue(generator = "UUID")
      @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
      @Column(name = "uid", updatable = false, nullable = false)
      private UUID uid;


      @Column(nullable = false)
      private String title;

      @Column(columnDefinition = "TEXT") // Prisma의 String은 길이 제한이 없으므로 TEXT 타입으로 지정
      private String context;

      @Column(nullable = false)
      private String identy;


      @Column(nullable = false)
      private String instructor;

      @CreationTimestamp
      @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
      private LocalDateTime createTime;

      @UpdateTimestamp
      @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
      private LocalDateTime updateTime;

      private LocalDateTime expirationTime;


      @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")    
      private int displayOrder = 0;
 
      @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
      private boolean delete = false;
  }