package com.project.Project.auth;

import com.project.Project.Util.CookieUtil;
import com.project.Project.auth.dto.Token;
import com.project.Project.auth.dto.UserDto;
import com.project.Project.service.impl.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserRequestMapper userRequestMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserDto userDto = userRequestMapper.toDto(oAuth2User);

        Token token = tokenService.generateToken(userDto.getEmail(), "USER");
        log.info("{}", token);
        writeTokenResponse(response, token);
    }

    private void writeTokenResponse(HttpServletResponse response, Token token) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Cookie accessTokenCookie = CookieUtil.createAccessTokenCookie(token.getToken());
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(token.getRefreshToken());
        response.addCookie(refreshTokenCookie);

        response.sendRedirect("/");
        var writer = response.getWriter();
//        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}