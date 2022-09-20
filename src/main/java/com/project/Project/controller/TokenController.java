package com.project.Project.controller;

import com.project.Project.Util.CookieUtil;
import com.project.Project.Util.JsonResult;
import com.project.Project.config.auth.dto.Token;
import com.project.Project.service.impl.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    // 토큰이 만료되었을 경우 사용자에게 안내해주기
    @GetMapping("/token/expired")
    public ResponseEntity<JsonResult> auth() {
        String message = "토큰이 만료되었습니다.";
        JsonResult jsonResult = new JsonResult(HttpStatus.UNAUTHORIZED, message, "/token/refresh");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(jsonResult);
    }

    @GetMapping("/token/refresh")
    public String refreshAuth(@CookieValue("refreshToken") String token, HttpServletRequest request, HttpServletResponse response) {

        if (token != null && tokenService.verifyToken(token)) {
            String email = tokenService.getUid(token);
            Token newToken = tokenService.generateToken(email, "USER");

            Cookie accessTokenCookie = CookieUtil.createAccessTokenCookie(newToken.getToken());
            response.addCookie(accessTokenCookie);

            Cookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(newToken.getRefreshToken());
            response.addCookie(refreshTokenCookie);

            response.setContentType("application/json;charset=UTF-8");

            // redirect로 변경
            return "HAPPY NEW TOKEN";
        }

        // refresh 토큰조차 만료된 경우 클라이언트에게 안내해주기.
        throw new RuntimeException();
    }

    // test
    @GetMapping("/valid")
    public String validUser(@CookieValue("accessToken") String token) {
        String email = tokenService.getUid(token);
        return "email : " + email;
    }

    /**
     * /token/free?email=abscce@naver.com 등으로 요청
     * @param email : 사용자 이메일
     * @return
     */
    @GetMapping("/token/free")
    public String freeToken(@RequestParam String email, HttpServletResponse response) throws IOException {
        Token token = tokenService.generateToken(email, "USER");
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Cookie accessTokenCookie = CookieUtil.createAccessTokenCookie(token.getToken());
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(token.getRefreshToken());
        response.addCookie(refreshTokenCookie);

        return "FREE TOKEN!";
    }
}
