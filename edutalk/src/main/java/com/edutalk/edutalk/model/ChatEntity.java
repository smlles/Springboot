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
  @Table(name = "chat", uniqueConstraints = {
      @UniqueConstraint(columnNames = {"identy", "instructorID", "studentID"})     
  })
  @Getter
  @Setter
  public class ChatEntity {


      @Id
      @GeneratedValue(generator = "UUID")
      @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
      @Column(name = "uid", updatable = false, nullable = false)
      private UUID uid;

      @Column(nullable = false)
      private String identy;

      private String lastChat;

      @CreationTimestamp
      @Column(name = "creat_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
      private LocalDateTime createTime;


      @UpdateTimestamp
      @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
      private LocalDateTime updateTime;

      @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
      private String instructorID;
     
      @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
      private String instructorName;
     
     
      @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
      private String studentID;
     
      @Column(nullable = false, columnDefinition = "TEXT DEFAULT ''")
      private String studentName;

      private String lastChatSender;
  }