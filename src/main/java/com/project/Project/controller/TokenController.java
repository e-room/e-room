package com.project.Project.controller;

import com.project.Project.Util.CookieUtil;
import com.project.Project.config.auth.dto.Token;
import com.project.Project.service.impl.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    // 응답 완성하기
    @GetMapping("/token/expired")
    public String auth() {
        throw new RuntimeException();
    }

    @GetMapping("/token/refresh")
    public String refreshAuth(@CookieValue("refreshToken") String fooCookie, HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Refresh");

        if (token != null && tokenService.verifyToken(token)) {
            String email = tokenService.getUid(token);
            Token newToken = tokenService.generateToken(email, "USER");

            Cookie accessTokenCookie = CookieUtil.createAccessTokenCookie(newToken.getToken());
            response.addCookie(accessTokenCookie);

            Cookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(newToken.getRefreshToken());
            response.addCookie(refreshTokenCookie);

            response.setContentType("application/json;charset=UTF-8");

            return "HAPPY NEW TOKEN";
        }

        throw new RuntimeException();
    }
}
