package com.project.Project.auth.handler;

import com.project.Project.auth.dto.MemberDto;
import com.project.Project.auth.dto.Token;
import com.project.Project.auth.enums.MemberRole;
import com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.project.Project.auth.service.TokenService;
import com.project.Project.config.properties.SecurityProperties;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.serializer.member.MemberSerializer;
import com.project.Project.util.component.CookieUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.IS_LOCAL;
import static com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@AllArgsConstructor
public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private TokenService tokenService;
    private MemberRepository memberRepository;
    private OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        MemberDto memberDto = MemberSerializer.toDto(oAuth2User);
        Token token = tokenService.generateToken(memberDto.getEmail(), memberDto.getAuthProviderType(), MemberRole.USER);

        memberRepository.findByEmailAndAuthProviderType(memberDto.getEmail(), memberDto.getAuthProviderType())
                .ifPresent((member -> {
                    member.setRefreshToken(token.getRefreshToken());
                    memberRepository.save(member);
                }));
        log.info("{}", token);


        Boolean isLocal = CookieUtil.getCookie(request, IS_LOCAL)
                .map(Cookie::getValue)
                .map(Boolean::parseBoolean).orElse(false);

        writeTokenResponse(request, response, token);
        String targetUrl = this.determineTargetUrlDelegate(request, response, token, isLocal);
        this.clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    protected String determineTargetUrlDelegate(HttpServletRequest request, HttpServletResponse response, Token token, boolean isLocal) throws IOException {
        if (isLocal) {
            String defaultUrl = new URIBuilder().setScheme(securityProperties.getScheme()).setPort(securityProperties.getPort()).setHost(securityProperties.getDefaultHost()).setPath(securityProperties.getDefaultSuccessPath()).toString();
            String redirectPath = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                    .map(Cookie::getValue)
                    .orElse(securityProperties.getDefaultSuccessPath());
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("accessToken", token.getAccessToken());
            params.add("refreshToken", token.getRefreshToken());
            params.add("redirectPath", redirectPath);
            String targetUrl = UriComponentsBuilder.newInstance()
                    .path(defaultUrl)
                    .queryParams(params).build().toString();
            return targetUrl;
        } else {
            return this.determineTargetUrl(request, response);
        }
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        String defaultUrl = new URIBuilder().setScheme(securityProperties.getScheme()).setPort(securityProperties.getPort()).setHost(securityProperties.getDefaultHost()).setPath(securityProperties.getDefaultSuccessPath()).toString();
        Optional<String> redirectUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .map((path) -> new URIBuilder().setScheme(securityProperties.getScheme()).setPort(securityProperties.getPort()).setHost(securityProperties.getDefaultHost()).setPath(path).toString());
        String targetUrl = redirectUrl.orElse(defaultUrl);
        return targetUrl;
    }

    private void writeTokenResponse(HttpServletRequest request, HttpServletResponse response, Token token) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Boolean isLocal = CookieUtil.getCookie(request, IS_LOCAL)
                .map(Cookie::getValue)
                .map(Boolean::parseBoolean).orElse(false);

        ResponseCookie accessTokenCookie = CookieUtil.createAccessTokenCookie(token.getAccessToken(), isLocal);
        ResponseCookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(token.getRefreshToken(), isLocal);
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}