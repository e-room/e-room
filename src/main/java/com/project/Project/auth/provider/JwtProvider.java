package com.project.Project.auth.provider;

import com.project.Project.Util.CookieUtil;
import com.project.Project.auth.JwtAuthentication;
import com.project.Project.auth.dto.MemberDto;
import com.project.Project.auth.dto.Token;
import com.project.Project.auth.exception.JwtException;
import com.project.Project.auth.service.TokenService;
import com.project.Project.domain.Member;
import com.project.Project.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;

@Component
@Slf4j
public class JwtProvider {

    private final MemberService memberService;
    private final TokenService tokenService;
    private final String secretKey;
    private final long tokenPeriod;
    private final long refreshPeriod;


    public JwtProvider(@Value("${jwt.secret-key}") String secretKey,
                       @Value("${jwt.token-validity-in-sec}") long tokenPeriod,
                       @Value("${jwt.refresh-token-validity-in-sec}") long refreshPeriod,
                       MemberService memberService, TokenService tokenService) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.memberService = memberService;
        this.tokenService = tokenService;
        this.tokenPeriod = tokenPeriod * 1000;
        this.refreshPeriod = refreshPeriod * 1000;
    }

    public JwtAuthentication authenticate(HttpServletRequest request, HttpServletResponse response, String accessToken, String refreshToken) {
        TokenService.JwtCode status = validateToken(accessToken);
        if (status.equals(TokenService.JwtCode.ACCESS)) {
            JwtAuthentication auth = getAuthentication(accessToken);
            return auth;
        } else if (status.equals(TokenService.JwtCode.EXPIRED)) {
            /*
                refresh token 가지고 access token 발급
            */
            if (refreshToken != null && validateToken(refreshToken) == TokenService.JwtCode.ACCESS) {
                Token newToken = tokenService.reissueToken(refreshToken);
                if (newToken != null) {
                    Cookie accessTokenCookie = CookieUtil.createAccessTokenCookie(newToken.getToken());
                    response.addCookie(accessTokenCookie);
                    Cookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(newToken.getRefreshToken());
                    response.addCookie(refreshTokenCookie);
                    // access token 생성
                    JwtAuthentication authentication = getAuthentication(newToken.getToken());
                    return authentication;
                }
            }
        }
        throw new JwtException("invalid Token");
    }

    @Transactional
    JwtAuthentication getAuthentication(String token) {
        String email = tokenService.getUid(token);
        Member member = memberService.findByEmail(email).get(); // 추후 예외처리

        MemberDto memberDto = MemberDto.builder()
                .email(email)
                .name(member.getName())
                .picture(member.getProfileImageUrl()).build();
        return new JwtAuthentication(
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")),
                memberDto, member,
                member.getRefreshToken()
        );
    }

    private TokenService.JwtCode validateToken(String token) {
        return this.tokenService.verifyToken(token);
    }
}