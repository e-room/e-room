package com.project.Project.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.auth.dto.MemberDto;
import com.project.Project.auth.dto.Token;
import com.project.Project.auth.dto.UidDto;
import com.project.Project.auth.service.TokenService;
import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.member.Member;
import com.project.Project.serializer.member.MemberSerializer;
import com.project.Project.util.JsonResult;
import com.project.Project.util.component.CookieUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Tag(name = "Token API", description = "토큰 만료, 재발급, 검증")
@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    // 토큰이 만료되었을 경우 사용자에게 안내해주기
    @GetMapping("/token/expired")
    public ResponseEntity<JsonResult> auth() {
        String message = "로그인이 필요합니다.";
        JsonResult jsonResult = new JsonResult(HttpStatus.UNAUTHORIZED, message, "/token/refresh");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(jsonResult);
    }

    @GetMapping("/token/refresh")
    public String refreshAuth(@CookieValue("refreshToken") String token, HttpServletRequest request, HttpServletResponse response) {

        if (token != null && tokenService.verifyToken(token).equals(TokenService.JwtCode.EXPIRED)) {
            UidDto uidDto = tokenService.getUid(token);
            Token newToken = tokenService.generateToken(uidDto.getEmail(), uidDto.getAuthProviderType(), "USER");

            ResponseCookie accessTokenCookie = CookieUtil.createAccessTokenCookie(newToken.getAccessToken());
            ResponseCookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(newToken.getRefreshToken());

            response.addHeader("Set-Cookie", accessTokenCookie.toString());
            response.addHeader("Set-Cookie", refreshTokenCookie.toString());

            response.setContentType("application/json;charset=UTF-8");

            // redirect로 변경
            return "HAPPY NEW TOKEN";
        }

        // refresh 토큰조차 만료된 경우 클라이언트에게 안내해주기.
        throw new RuntimeException();
    }

    @GetMapping("/token/valid")
    public ResponseEntity<MemberDto> validUser(@AuthUser Member member) {
        return ResponseEntity.ok(MemberSerializer.toDto(member));
    }

    /**
     * /token/free?email=abscce@naver.com 등으로 요청
     *
     * @param email : 사용자 이메일
     * @return
     */
    @Deprecated
    @GetMapping("/token/free")
    public String freeToken(@RequestParam String email, HttpServletResponse response) throws IOException {
        Token token = tokenService.generateToken(email, AuthProviderType.KAKAO,"USER");
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        ResponseCookie accessTokenCookie = CookieUtil.createAccessTokenCookie(token.getAccessToken());
        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(token.getRefreshToken());

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return "FREE TOKEN!";
    }
}
