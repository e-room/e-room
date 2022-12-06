package com.project.Project.util.component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieUtil {
    private static int accessTokenMaxAge = 60 * 30; // 30분
    private static int refreshTokenMaxAge = 60 * 60 * 24 * 30 * 1; // 1개월

    public static Cookie createAccessTokenCookie(String token) {
        Cookie accessTokenCookie = new Cookie("accessToken", token);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setMaxAge(accessTokenMaxAge);

        return accessTokenCookie;
    }

    public static Cookie createRefreshTokenCookie(String token) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", token);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(refreshTokenMaxAge);

        return refreshTokenCookie;
    }
}
