package com.korea.book.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class NaverBookApiController {

    private final WebClient webClient;
    
    //application.properties 또는 application.yml파일에서
    //값을 읽어온다
    @Value("${naver.client.id}")
    private String clientId;
    
    @Value("${naver.client.secret}")
    private String clientSecret;
    
    public NaverBookApiController(WebClient.Builder webClientBuilder) {
       this.webClient = webClientBuilder
                      .baseUrl(
                         "https://openapi.naver.com/v1/search")
                      .build();
    }
    
    @GetMapping("/api/books")
    public Mono<String> searchBook(@RequestParam String query){
       //WebClient를 통해 네이버 도서검색API에 GET요청을 보낸다.
       return webClient.get()
             .uri(uriBuilder -> uriBuilder.path("/book.json")
                                  .queryParam("query", query)
                                  .build())
             //요청 헤더에 Content-Type을 JSON으로 설정
             .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
             //네이버 API인증을 위한 Client ID와 Secret을 헤더에 추가
             .header("X-Naver-Client-Id", clientId)
             .header("X-Naver-Client-Secret", clientSecret)
             .retrieve()
             .bodyToMono(String.class);//응답 데이터를 Mono 객체로 받음;
    }
    
    
    
    
}
