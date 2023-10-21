package com.project.Project.common.util.component;

import com.project.Project.config.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Optional;

//@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
@RequiredArgsConstructor
public class CookieUtil {
    private static int accessTokenMaxAge = 60 * 30; // 30분
    private static int refreshTokenMaxAge = 60 * 60 * 24 * 30 * 1; // 1개월
    private final SecurityProperties securityProperties;
    private static SecurityProperties staticSecurityProperties;

    @PostConstruct
    public void init() {
        staticSecurityProperties = this.securityProperties;
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }

    public static ResponseCookie createAccessTokenCookie(String token) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", token)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .maxAge(accessTokenMaxAge)
                .build();
        return accessTokenCookie;
    }

    public static ResponseCookie createRefreshTokenCookie(String token) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", token)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .maxAge(refreshTokenMaxAge)
                .build();
        return refreshTokenCookie;
    }

    public static ResponseCookie createAccessTokenCookie(String token, Boolean isLocal) {
        ResponseCookie.ResponseCookieBuilder accessTokenCookie = ResponseCookie.from("accessToken", token)
                .secure(true)
                .httpOnly(true)
                .domain(isLocal ? "localhost" : staticSecurityProperties.getDefaultHost())
                .path("/")
                .sameSite("None")
                .maxAge(accessTokenMaxAge);
        return accessTokenCookie.build();
    }

    public static ResponseCookie createRefreshTokenCookie(String token, Boolean isLocal) {
        ResponseCookie.ResponseCookieBuilder refreshTokenCookie = ResponseCookie.from("refreshToken", token)
                .secure(true)
                .httpOnly(true)
                .domain(isLocal ? "localhost" : staticSecurityProperties.getDefaultHost())
                .path("/")
                .sameSite("None")
                .maxAge(refreshTokenMaxAge);
        return refreshTokenCookie.build();
    }

    public static ResponseCookie createDeleteTokenCookie(String name, Boolean isLocal) {
        return ResponseCookie.from(name, null)
                .secure(true)
                .httpOnly(true)
                .domain(isLocal ? "localhost" : staticSecurityProperties.getDefaultHost())
                .path("/")
                .sameSite("None")
                .maxAge(0)
                .build();
    }
}
