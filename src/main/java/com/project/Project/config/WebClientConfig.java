package com.project.Project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient kakaoMapWebClient() {
        return WebClient.builder()
                .baseUrl("https://dapi.kakao.com/v2/local/search/address.json")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK 20aa1dac798137cbd72364962f49da7f")
                .build();
    }

    @Bean
    WebClient nickNameWebClient() {
        return WebClient.builder()
                .baseUrl("https://nickname.hwanmoo.kr/")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
