package com.project.Project.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.auth.authentication.JwtAuthentication;
import com.project.Project.auth.dto.MemberDto;
import com.project.Project.auth.dto.Token;
import com.project.Project.auth.dto.UidDto;
import com.project.Project.auth.exception.JwtAuthenctionException;
import com.project.Project.auth.handler.JWTFailureHandler;
import com.project.Project.auth.service.TokenService;
import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.member.Member;
import com.project.Project.exception.ErrorCode;
import com.project.Project.serializer.member.MemberSerializer;
import com.project.Project.util.JsonResult;
import com.project.Project.util.component.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.IS_LOCAL;

@Tag(name = "Token API", description = "토큰 만료, 재발급, 검증")
@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JWTFailureHandler failureHandler;

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

    private String getCookieValue(HttpServletRequest req, String cookieName) {
        if (req.getCookies() == null) return null;
        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void postAuthenticate(HttpServletRequest request, HttpServletResponse response, Authentication authenticationResult) {
        Boolean isLocal = CookieUtil.getCookie(request, IS_LOCAL)
                .map(Cookie::getValue)
                .map(Boolean::parseBoolean).orElse(false);
        JwtAuthentication jwtAuthenticationResult = (JwtAuthentication) authenticationResult;
        ResponseCookie accessTokenCookie = CookieUtil.createAccessTokenCookie(jwtAuthenticationResult.getToken().getAccessToken(), isLocal);
        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(jwtAuthenticationResult.getToken().getRefreshToken(), isLocal);
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        System.out.println("refersh: "+refreshTokenCookie);
    }

    @Operation(summary = "토큰 검증", description = "토큰 유효성 검사 API")
    @GetMapping("/token/valid")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    public ResponseEntity<TokenResponseDto.TokenValidDto> validUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Boolean isValid = true;
        String accessToken = getCookieValue(request, "accessToken");
        String refreshToken = getCookieValue(request, "refreshToken");
        if(accessToken != null) {
            try {
                TokenService.JwtCode jwtCode = tokenService.verifyToken(accessToken);
                if(jwtCode != TokenService.JwtCode.ACCESS) isValid = false;
            } catch (Exception e) {
                isValid = false;
            }
        }
        else if(refreshToken != null) {
            try {
                Token jwtToken = new Token(accessToken, refreshToken);
                JwtAuthentication authenticationRequest = new JwtAuthentication(jwtToken, request, response);
                JwtAuthentication authenticationResult = (JwtAuthentication) this.authenticationManager.authenticate(authenticationRequest);
                SecurityContextHolder.getContext().setAuthentication(authenticationResult);
                postAuthenticate(request, response, authenticationResult);
            } catch (JwtAuthenctionException ex) {
                SecurityContextHolder.clearContext();
                this.failureHandler.onAuthenticationFailure(request, response, ex);
                throw ex;
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                JwtAuthenctionException authenticationException = new JwtAuthenctionException("jwt인증에 실패했습니다", ex.getCause(), ErrorCode.JWT_BAD_REQUEST);
                this.failureHandler.onAuthenticationFailure(request, response, authenticationException);
                throw authenticationException;
            }
        }
        else isValid = false;
        return ResponseEntity.ok(TokenResponseDto.TokenValidDto.builder().isValid(isValid).build());
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
