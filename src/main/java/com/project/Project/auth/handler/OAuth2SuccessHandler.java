package com.project.Project.auth.handler;

import com.project.Project.Util.CookieUtil;
import com.project.Project.auth.dto.MemberDto;
import com.project.Project.auth.dto.Token;
import com.project.Project.auth.service.TokenService;
import com.project.Project.domain.Member;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.serializer.member.MemberSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        MemberDto memberDto = MemberSerializer.toDto(oAuth2User);
        Token token = tokenService.generateToken(memberDto.getEmail(), "USER");
        Member member = memberRepository.findByEmail(memberDto.getEmail()).get();
        member.setRefreshToken(token.getRefreshToken());
        memberRepository.save(member);
        log.info("{}", token);
        writeTokenResponse(response, token);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void writeTokenResponse(HttpServletResponse response, Token token) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        Cookie accessTokenCookie = CookieUtil.createAccessTokenCookie(token.getAccessToken());
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = CookieUtil.createRefreshTokenCookie(token.getRefreshToken());
        response.addCookie(refreshTokenCookie);

//        var writer = response.getWriter();
////        writer.println(objectMapper.writeValueAsString(token));
//        writer.flush();
    }
}