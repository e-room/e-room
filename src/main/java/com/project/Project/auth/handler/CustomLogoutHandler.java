package com.project.Project.auth.handler;

import com.project.Project.util.component.CookieUtil;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {
    private ResponseCookie createDeleteTokenCookie(String name) {
        return ResponseCookie.from(name, "")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .build();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        ResponseCookie accessTokenCookie = createDeleteTokenCookie("accessToken");
        ResponseCookie refreshTokenCookie = createDeleteTokenCookie("refreshToken");

        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
