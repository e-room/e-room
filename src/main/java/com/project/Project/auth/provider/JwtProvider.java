package com.project.Project.auth.provider;

import com.project.Project.auth.authentication.JwtAuthentication;
import com.project.Project.auth.dto.MemberDto;
import com.project.Project.auth.dto.Token;
import com.project.Project.auth.dto.UidDto;
import com.project.Project.auth.exception.JwtAuthenctionException;
import com.project.Project.auth.service.TokenService;
import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.member.Member;
import com.project.Project.exception.ErrorCode;
import com.project.Project.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Collections;

@Component
@Slf4j
public class JwtProvider implements AuthenticationProvider {

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

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthentication jwtAuthenticationToken = (JwtAuthentication) authentication;
        Token token = ((JwtAuthentication) authentication).getToken();
        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();
        TokenService.JwtCode status;
        if (accessToken != null) {
            status = validateToken(accessToken);
        } else {
            status = TokenService.JwtCode.EXPIRED;
        }
        if (status.equals(TokenService.JwtCode.ACCESS)) {
            setAuthMetadata(token, jwtAuthenticationToken);
            return jwtAuthenticationToken;
        } else if (status.equals(TokenService.JwtCode.EXPIRED)) {
            // refresh token 가지고 access token 발급
            if (refreshToken == null)
                throw new JwtAuthenctionException("토큰 재발급을 위해선 refreshToken이 필요합니다.", ErrorCode.JWT_BAD_REQUEST);
            TokenService.JwtCode code = validateToken(refreshToken);
            if (code == TokenService.JwtCode.DENIED) throw new JwtAuthenctionException(ErrorCode.JWT_BAD_REQUEST);
            if (code == TokenService.JwtCode.EXPIRED)
                throw new JwtAuthenctionException(ErrorCode.JWT_ALL_TOKEN_EXPIRED);
            if (validateToken(refreshToken) == TokenService.JwtCode.ACCESS) {
                Token newToken = tokenService.reissueToken(refreshToken);
                setAuthMetadata(newToken, jwtAuthenticationToken);
                return jwtAuthenticationToken;
            }
        } else {
            throw new JwtAuthenctionException(ErrorCode.JWT_DENIED);
        }
        throw new JwtAuthenctionException(ErrorCode.JWT_BAD_REQUEST);
    }

    @Transactional
    public void setAuthMetadata(Token token, JwtAuthentication authentication) {
        UidDto uidDto = tokenService.getUid(token.getAccessToken());
        String email = uidDto.getEmail();
        AuthProviderType authProviderType = uidDto.getAuthProviderType();
        Member member = memberService.findByEmailAndAuthProviderType(email, authProviderType).get(); // 추후 예외처리
        MemberDto memberDto = MemberDto.builder()
                .email(email)
                .name(member.getName())
                .picture(member.getProfileImage().getUrl())
                .authProviderType(member.getAuthProviderType())
                .build();
        authentication.setToken(token);
        authentication.setAuthenticated(true);
        authentication.setPrincipal(memberDto);
        authentication.setPrincipalDetails(member);
        authentication.setAuthorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

    }

    private TokenService.JwtCode validateToken(String token) {
        return this.tokenService.verifyToken(token);
    }
}