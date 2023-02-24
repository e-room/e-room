package com.project.Project.auth.service;

import com.project.Project.auth.dto.Token;
import com.project.Project.auth.dto.UidDto;
import com.project.Project.auth.enums.MemberRole;
import com.project.Project.auth.exception.JwtAuthenticationException;
import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.member.Member;
import com.project.Project.exception.ErrorCode;
import com.project.Project.service.member.MemberService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
public class TokenService {

    public static enum JwtCode {
        DENIED,
        ACCESS,
        EXPIRED;
    }

    private String secretKey;
    private final Long tokenPeriod;
    private final Long refreshPeriod;
    private final MemberService memberService;

//    @PostConstruct
//    protected void init() {
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }

    public TokenService(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.token-validity-in-sec}") long tokenPeriod,
            @Value("${jwt.refresh-token-validity-in-sec}") long refreshPeriod,
            MemberService memberService) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.tokenPeriod = tokenPeriod * 1000;
        this.refreshPeriod = refreshPeriod * 1000;
        this.memberService = memberService;
    }

    //    @Override
    public Token generateToken(String email, AuthProviderType authProviderType, MemberRole role) {
        String subject = email + "," + authProviderType.name();

        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("role", role.getAuthority());

        Date now = new Date();
        return new Token(this.generateAccessToken(email, authProviderType, role),
                this.generateRefreshToken(email, authProviderType, role));
    }

    private String generateAccessToken(String email, AuthProviderType authProviderType, MemberRole role) {
        String subject = email + "," + authProviderType.name();

        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("role", role.getAuthority());

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private String generateRefreshToken(String email, AuthProviderType authProviderType, MemberRole role) {
        String subject = email + "," + authProviderType.name();

        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("role", role.getAuthority());

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public JwtCode verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            if (claims.getBody()
                    .getExpiration()
                    .before(new Date())) {
                throw new ExpiredJwtException(claims.getHeader(), claims.getBody(), "expired Token, reissue refresh Token");
            }
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e) {
            return JwtCode.EXPIRED;
        } catch (Exception e) {
            return JwtCode.DENIED;
        }
    }

    /**
     * @param token
     * @return email|authProviderType 형식의 문자열을 반환합니다. ex) hello@naver.com|NAVER
     */
    public UidDto getUid(String token) {
        String subject = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        return UidDto.builder()
                .email(subject.split(",")[0])
                .authProviderType(AuthProviderType.valueOf(subject.split(",")[1]))
                .build();
    }

    @Transactional
    public Token reissueToken(String refreshToken) throws RuntimeException {
        // refresh token을 디비의 그것과 비교해보기
        UidDto uidDto = getUid(refreshToken);
        String email = uidDto.getEmail();
        AuthProviderType authProviderType = uidDto.getAuthProviderType();
        Member member = memberService.findByEmailAndAuthProviderType(email, authProviderType).get(); // 추후 예외처리
        if (member.getRefreshToken().equals(refreshToken)) {
            // 새로운거 생성
            Token newToken = generateToken(email, authProviderType, MemberRole.USER);
            member.setRefreshToken(newToken.getRefreshToken());
            return newToken;
        } else {
            throw new JwtAuthenticationException("토큰 재발급에 실패했습니다.", ErrorCode.JWT_BAD_REQUEST);
        }
    }

    @Transactional
    public Token reissueToken(String refreshToken, JwtCode status) {
        UidDto uidDto = getUid(refreshToken);
        String email = uidDto.getEmail();
        AuthProviderType authProviderType = uidDto.getAuthProviderType();
        Member member = memberService.findByEmailAndAuthProviderType(email, authProviderType).get(); // 추후 예외처리

        if (status.equals(JwtCode.ACCESS)) {
            if (!member.equals(email, authProviderType))
                throw new JwtAuthenticationException("요청한 refreshToken이 멤버 정보와 일치하지 않습니다.", ErrorCode.JWT_BAD_REQUEST);
            String accessToken = this.generateAccessToken(email, authProviderType, MemberRole.USER);
            return new Token(accessToken, refreshToken);
        } else if (status.equals(JwtCode.EXPIRED)) {
            // 멤버 정보를 비교해서 해당하는 멤버에 대해 새롭게 발급하기
            if (!member.equals(email, authProviderType))
                throw new JwtAuthenticationException("요청한 refreshToken이 멤버 정보와 일치하지 않습니다.", ErrorCode.JWT_BAD_REQUEST);
            Token newToken = this.generateToken(email, authProviderType, MemberRole.USER);
            member.setRefreshToken(newToken.getRefreshToken());
            return newToken;
        } else {
            throw new JwtAuthenticationException("토큰 재발급에 실패했습니다.", ErrorCode.JWT_BAD_REQUEST);
        }
    }

}
