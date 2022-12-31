package com.project.Project.auth.dto;

import lombok.*;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Token {
    /**
     * 인증 토큰 : API 서버에 접근할 때 사용
     */
    private String accessToken;
    /**
     * 리프레쉬 토큰 : 인증 토큰이 만료되었을 경우 사용
     */
    private String refreshToken;
}
