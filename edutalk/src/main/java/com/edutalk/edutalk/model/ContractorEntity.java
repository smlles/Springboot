package com.edutalk.edutalk.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contractor")
@Getter
@Setter
public class ContractorEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "uid", updatable = false, nullable = false)
	private UUID uid;

	@Column(name = "identy", unique = true, nullable = false)
	private String identy;

	@ColumnDefault("teacher") 
	@Column(name = "type", nullable=false)
	private String type;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "name") // 새로 추가할 필드: 사용자 이름
	private String name;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "passwordSalt", nullable = false, columnDefinition =  "TEXT DEFAULT ''")
	private String passwordSalt;

	@Column(name = "publicKey", columnDefinition = "TEXT")
	private String publicKey;

	@Column(name = "privateKey", columnDefinition = "TEXT")
	private String privateKey;

	@Column(name = "periodTime")
	private LocalDateTime periodTime;

	@Column(name = "expirationTime")
	private LocalDateTime expirationTime;

	@Column(name = "emailExpires")
	private LocalDateTime emailExpires;

	@Column(name = "emailToken", columnDefinition = "TEXT")
	private String emailToken;

	@Column(name = "certified", nullable = false)
	private boolean certified = false;

	@Column(name = "origin")
	private String origin;
}