package com.project.Project.auth.handler;

import com.project.Project.Util.component.CookieUtil;
import com.project.Project.auth.dto.MemberDto;
import com.project.Project.auth.dto.Token;
import com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.project.Project.auth.service.TokenService;
import com.project.Project.config.properties.SecurityProperties;
import com.project.Project.domain.Member;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.serializer.member.MemberSerializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@AllArgsConstructor
public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private TokenService tokenService;
    private MemberRepository memberRepository;
    private OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        MemberDto memberDto = MemberSerializer.toDto(oAuth2User);
        Token token = tokenService.generateToken(memberDto.getEmail(), "USER");
        Member member = memberRepository.findByEmail(memberDto.getEmail()).get();
        member.setRefreshToken(token.getRefreshToken());
        memberRepository.save(member);
        log.info("{}", token);
        writeTokenResponse(request, response, token);
        response.setHeader("accessToken", token.getAccessToken());
        response.setHeader("refreshToken", token.getRefreshToken());
        String targetUrl = determineTargetUrl(request, response, authentication);
        this.clearAuthenticationAttributes(request, response);
//        response.getWriter().write("success");
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
//        super.onAuthenticationSuccess(request, response, authentication);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        if (Boolean.valueOf(request.getHeader("isLocal"))) {
            String defaultUrl = new URIBuilder().setScheme("http").setPort(3000).setHost("localhost").setPath(securityProperties.getDefaultSuccessPath()).toString();
            Optional<String> redirectUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                    .map(Cookie::getValue)
                    .map((path) -> new URIBuilder().setScheme("http").setPort(3000).setHost("localhost").setPath(path).toString());

            String targetUrl = redirectUrl.orElse(defaultUrl);
            return targetUrl;

        } else {
            String defaultUrl = new URIBuilder().setScheme("https").setHost(securityProperties.getDefaultHost()).setPath(securityProperties.getDefaultSuccessPath()).toString();
            Optional<String> redirectUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                    .map(Cookie::getValue)
                    .map((path) -> new URIBuilder().setScheme("https").setHost(securityProperties.getDefaultHost()).setPath(path).toString());

            String targetUrl = redirectUrl.orElse(defaultUrl);
            return targetUrl;
        }
    }

    private void writeTokenResponse(HttpServletRequest request, HttpServletResponse response, Token token) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        Cookie accessTokenCookie = CookieUtil.createAccessTokenCookie(token.getAccessToken());
        Cookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(token.getRefreshToken());
        if (Boolean.valueOf(request.getHeader("isLocal"))) {
            accessTokenCookie.setDomain("localhost");
            refreshTokenCookie.setDomain("localhost");
        }
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}