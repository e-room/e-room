package com.project.Project.auth.filter;

import com.project.Project.auth.authentication.JwtAuthentication;
import com.project.Project.auth.dto.Token;
import com.project.Project.auth.exception.JwtAuthenctionException;
import com.project.Project.auth.handler.JWTFailureHandler;
import com.project.Project.exception.ErrorCode;
import com.project.Project.util.component.CookieUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.IS_LOCAL;

/**
 * 발급받은 토큰을 이용하여 security 인증을 처리하는 필터
 */
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTFailureHandler failureHandler;

//    public JwtAuthFilter(AuthenticationManager authenticationManager) {
//
////        super(new AntPathRequestMatcher("/api/**"));
////        this.setAuthenticationFailureHandler(new JWTFailureHandler());
////        super.setAuthenticationManager(authenticationManager);
//    }

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
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getCookieValue(request, "accessToken");
        String refreshToken = getCookieValue(request, "refreshToken");
        if (accessToken == null && refreshToken == null) {
            filterChain.doFilter(request, response);
        } else {
            try {
                Token jwtToken = new Token(accessToken, refreshToken);
                JwtAuthentication authenticationRequest = new JwtAuthentication(jwtToken, request, response);
                JwtAuthentication authenticationResult = (JwtAuthentication) this.authenticationManager.authenticate(authenticationRequest);
                SecurityContextHolder.getContext().setAuthentication(authenticationResult);
            /*
                authenticate 관련 정책 추가 가능
                Ex) 초기 유저이기 때문에 실명인증이 필요하다, 개인정보 동의 체크, 알림 수신 체크 등등
            */
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
            filterChain.doFilter(request, response);
        }
    }
}
