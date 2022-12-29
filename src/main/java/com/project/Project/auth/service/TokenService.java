package com.project.Project.auth.service;

import com.project.Project.auth.dto.Token;
import com.project.Project.domain.member.Member;
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
    public Token generateToken(String uid, String role) {
        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);

        Date now = new Date();
        return new Token(
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact(),
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact());
    }

    public JwtCode verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            System.out.println(new Date().getTime());
            System.out.println(claims.getBody().getExpiration().getTime());
            if (claims.getBody()
                    .getExpiration()
                    .before(new Date())) {
                throw new ExpiredJwtException(claims.getHeader(), claims.getBody(), "expired Token, reissue refresh Token");
            }
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e) {
            return JwtCode.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("jwt Error: {}", e);
        }
        return JwtCode.DENIED;
    }

    public String getUid(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    @Transactional
    public Token reissueToken(String refreshToken) throws RuntimeException {
        // refresh token을 디비의 그것과 비교해보기
        String email = getUid(refreshToken);
        Member member = memberService.findByEmail(email).get(); // 추후 예외처리
        if (member.getRefreshToken().equals(refreshToken)) {
            // 새로운거 생성
            Token newToken = generateToken(email, "USER");
            member.setRefreshToken(newToken.getRefreshToken());
            return newToken;
        } else {
            log.info("refresh 토큰이 일치하지 않습니다. ");
            return null;
        }
    }
}
