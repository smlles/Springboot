package com.edutalk.edutalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.edutalk.edutalk.model") // ✅ Entity 위치 명시
@EnableJpaRepositories(basePackages = "com.edutalk.edutalk.repository") // ✅ Repository 위치 명시
public class EdutalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdutalkApplication.class, args);
	}

}