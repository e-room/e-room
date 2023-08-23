package com.project.Project.controller;

import com.project.Project.auth.authentication.JwtAuthentication;
import com.project.Project.auth.dto.Token;
import com.project.Project.auth.exception.JwtAuthenticationException;
import com.project.Project.auth.handler.JWTFailureHandler;
import com.project.Project.auth.service.TokenService;
import com.project.Project.exception.ErrorCode;
import com.project.Project.util.component.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
        System.out.println("refersh: " + refreshTokenCookie);
    }

    @CrossOrigin(allowCredentials = "false")
    @Operation(summary = "토큰 검증", description = "토큰 유효성 검사 API")
    @GetMapping("/token/valid")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    public ResponseEntity<TokenResponseDto.TokenValidDto> validUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Boolean isValid = true;
        String accessToken = getCookieValue(request, "accessToken");
        String refreshToken = getCookieValue(request, "refreshToken");
        if (accessToken != null) {
            try {
                TokenService.JwtCode jwtCode = tokenService.verifyToken(accessToken);
                if (jwtCode != TokenService.JwtCode.ACCESS) isValid = false;
            } catch (Exception e) {
                isValid = false;
            }
        } else if (refreshToken != null) {
            try {
                Token jwtToken = new Token(accessToken, refreshToken);
                JwtAuthentication authenticationRequest = new JwtAuthentication(jwtToken, request, response);
                JwtAuthentication authenticationResult = (JwtAuthentication) this.authenticationManager.authenticate(authenticationRequest);
                SecurityContextHolder.getContext().setAuthentication(authenticationResult);
                postAuthenticate(request, response, authenticationResult);
            } catch (JwtAuthenticationException ex) {
                SecurityContextHolder.clearContext();
                this.failureHandler.onAuthenticationFailure(request, response, ex);
                throw ex;
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                JwtAuthenticationException authenticationException = new JwtAuthenticationException("jwt인증에 실패했습니다", ex.getCause(), ErrorCode.JWT_BAD_REQUEST);
                this.failureHandler.onAuthenticationFailure(request, response, authenticationException);
                throw authenticationException;
            }
        } else isValid = false;
        return ResponseEntity.ok(TokenResponseDto.TokenValidDto.builder().isValid(isValid).build());
    }
}
