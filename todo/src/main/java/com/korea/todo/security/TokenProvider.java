package com.korea.todo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.korea.todo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {
	
	//비밀키
	private static final String SECRET_KEY = 
			"6323895b07509187f51dd8b6cf69c358a0701016582d6d2aa6844a781f083f16afae293e100e1378e05441bb08dadba0a8035093e468d31c54bf614a9f87acee783029f5cc449a5023d373f1299c139971f9c5985fc5a048e47c5254e5485f55872db765dcf2d959101ce0129c5218c0e4e77c60a3df7b352daa642c952ed370480872f5415916c1882ddff1ff602b947a94e70224c2da05cc743660e1ee7bac7ba982968fff47c02e1e0526a22bd14551386fdcff21feadc198099301a0a47e7bb2488fa17031f4cb82c65141972c2443d35f4f2e0ce02de3711798e1af9d79e76868e164a9f66772eff360b023141fd68edd12747ee3bf1a36db913cf3983f70f715340d3f3ea2c57a807683b3afbcab0378b133e8bd6086020ca28a54c181dad1af96ff3e26cf62a550329cf9ff6fe09df58ef45492e1658b20c738c0192157856e7403cb4f16e61013eab926d7f8c0f50a62c87816636dce35a36d4755eea101ab3f99d7e1f5bedee5cd690e0385cbacb58a8b68cf000046b33f96bbde5ecca71b4251e64a0eee4c360477a22d7f237fb8e68c4b7f7bd6d27452b6a8cb21d6cf6833d94bbb7ca41a5f0b20451f08e1bfaf58740f50862fbbfc2bd9193a05dc0689aad71a1f7db6f444ac2a21cf3cbd04cdd6f788332817d07329bec06e7608e47af84a9221d7e97888f38a1f6aa29d04c3df6aa78b3d4f9f0f34b46df8f6";
	
	//토큰을 만드는 메서드 
	//JWT라이브러리를 이용해 JWT 토큰 생성.
	//토큰 생성과정에서 우리가 임의로 지정한 SECRET_KEY 를 개인키로 사용한다.
	public String create(UserEntity entity) {
		//토큰 만료시간을 설정
		//현재 시각 +1일 
		
		//Instant 클래스 : 타임 스탬프로 찍는다.
		//plus() : 첫번째 인자= 더할 양 , 두번째 인자 시간단위 
		//ChronoUnit 열거형의 DAYS : 일 단위를 의미
		Date expiryDate = Date.from(Instant.now().plus(1,ChronoUnit.DAYS));
		
		/*
		 * header
		 * {
		 * "alg":"HS512"
		 * }.
		 * {
		 * "sub":"4028838596b35ec90196b367f6570001",
		 * "iss":"todo app",
		 * "iat":1595733657,
		 * "exp":1596597657
		 * }.
		 * 서명
		 * */
		
		//JWT 토큰생성
		return Jwts.builder()
				//header에 들어갈 내용 및 서명을 할 SECRET_KEY
				.signWith(SignatureAlgorithm.HS512,SECRET_KEY) //헤더 + 서명 알고리즘 설정
				.setSubject(entity.getId())//sub 클레임 : 사용자 고유 id
				.setIssuer("todo app") // iss 클레임 : 토큰 발급자
				.setIssuedAt(new Date()) // iat 클레임 : 발급 시각
				.setExpiration(expiryDate) // exp 클레임 : 만료 시각
				.compact(); // 최종 직렬화 된 토큰 문자열을 반환
				
	}
	
	//토큰을 디코딩,파싱 및 위조 여부 확인
	//그리고 우리가 원하는 유저의 ID를 반환한다.
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parser()
							.setSigningKey(SECRET_KEY) //서명 검증용 키 설정
							.parseClaimsJws(token) //토큰 파싱 및 서명 검증
							.getBody(); //내부 페이로드(claims) 획득
		
		return claims.getSubject();//sub 클레임 (사용자ID)반환		
	}
	
}
