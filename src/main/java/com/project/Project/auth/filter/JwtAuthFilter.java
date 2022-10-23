package com.project.Project.auth.filter;

import com.project.Project.auth.JwtAuthentication;
import com.project.Project.auth.exception.JwtException;
import com.project.Project.auth.provider.JwtProvider;
import com.project.Project.auth.service.TokenService;
import com.project.Project.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 발급받은 토큰을 이용하여 security 인증을 처리하는 필터
 */
@Component
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {


    private final TokenService tokenService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public JwtAuthFilter(TokenService tokenService, MemberService memberService, JwtProvider jwtProvider) {
        super(new AntPathRequestMatcher("/**"));
        this.tokenService = tokenService;
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
        this.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/token/expired"));
    }

    private String getCookieValue(HttpServletRequest req, String cookieName) {
        if (req.getCookies() == null) return null;
        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String accessToken = getCookieValue((HttpServletRequest) request, "accessToken");
        String refreshToken = getCookieValue((HttpServletRequest) request, "refreshToken");

        if (accessToken != null) {
            throw new JwtException("no accessToken");
        }
        JwtAuthentication authentication = this.jwtProvider.authenticate(request, response, accessToken, refreshToken);
        /*
            authenticate 관련 정책 추가 가능
            Ex) 초기 유저이기 때문에 실명인증이 필요하다, 개인정보 동의 체크, 알림 수신 체크 등등
         */
        return authentication;
    }
}
