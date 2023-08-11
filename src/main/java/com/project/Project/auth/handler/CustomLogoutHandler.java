package com.project.Project.auth.handler;

import com.project.Project.common.util.component.CookieUtil;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.IS_LOCAL;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Boolean isLocal = CookieUtil.getCookie(request, IS_LOCAL)
                .map(Cookie::getValue)
                .map(Boolean::parseBoolean).orElse(false);

        ResponseCookie accessTokenCookie = CookieUtil.createDeleteTokenCookie("accessToken", isLocal);
        ResponseCookie refreshTokenCookie = CookieUtil.createDeleteTokenCookie("refreshToken", isLocal);

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
