package com.project.Project.auth.filter;

import com.project.Project.Util.CookieUtil;
import com.project.Project.auth.JwtAuthentication;
import com.project.Project.auth.dto.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 발급받은 토큰을 이용하여 security 인증을 처리하는 필터
 */
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/**"));
        this.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/token/expired"));
        super.setAuthenticationManager(authenticationManager);
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
        String accessToken = getCookieValue(request, "accessToken");
        String refreshToken = getCookieValue(request, "refreshToken");
        Token jwtToken = new Token(accessToken, refreshToken);
        Object authenticationDetails = this.authenticationDetailsSource.buildDetails(request);
        JwtAuthentication authenticationRequest = new JwtAuthentication(jwtToken, request, response);
        authenticationRequest.setDetails(authenticationDetails);
//        JwtAuthentication authentication = this.jwtProvider.authenticate(request, response, accessToken, refreshToken);
        JwtAuthentication authenticationResult = (JwtAuthentication) this.getAuthenticationManager().authenticate(authenticationRequest);
        /*
            authenticate 관련 정책 추가 가능
            Ex) 초기 유저이기 때문에 실명인증이 필요하다, 개인정보 동의 체크, 알림 수신 체크 등등
         */
        postAuthenticate(request, response, authenticationResult);
        return authenticationResult;
    }

    private void postAuthenticate(HttpServletRequest request, HttpServletResponse response, Authentication authenticationResult) {
        JwtAuthentication jwtAuthenticationResult = (JwtAuthentication) authenticationResult;
        Cookie accessTokenCookie = CookieUtil.createAccessTokenCookie(jwtAuthenticationResult.getToken().getAccessToken());
        response.addCookie(accessTokenCookie);
        Cookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(jwtAuthenticationResult.getToken().getRefreshToken());
        response.addCookie(refreshTokenCookie);
    }

}
