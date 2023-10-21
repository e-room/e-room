package com.project.Project.controller.admin.controller;

import com.project.Project.auth.dto.Token;
import com.project.Project.auth.enums.MemberRole;
import com.project.Project.auth.handler.OAuth2SuccessHandler;
import com.project.Project.auth.service.TokenService;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.member.MemberException;
import com.project.Project.common.util.component.CookieUtil;
import com.project.Project.config.properties.SecurityProperties;
import com.project.Project.controller.admin.dto.AdminRequestDto;
import com.project.Project.controller.admin.dto.AdminResponseDto;
import com.project.Project.domain.member.Member;

import com.project.Project.serializer.admin.AdminSerializer;
import com.project.Project.service.admin.AdminService;
import com.project.Project.service.member.MemberService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.IS_LOCAL;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final TokenService tokenService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private SecurityProperties securityProperties;

    @PostMapping("/member")
    public ResponseEntity<AdminResponseDto.CreateMemberDto> createMember(@RequestBody AdminRequestDto.CreateMemberDto request) {
        Member createdMember = adminService.createMember(request);
        return ResponseEntity.ok(AdminSerializer.toCreateMemberDto(createdMember));
    }

    @PostMapping("/member/login")
    public ResponseEntity<?> login(@RequestBody AdminRequestDto.LoginMemberDto req, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<Member> optionalMember = memberService.findByEmail(req.getEmail());
        if(optionalMember.isPresent()) {
            Member member = optionalMember.get();
            Token token = tokenService.generateToken(member.getEmail(), member.getAuthProviderType(), MemberRole.USER);
            memberService.updateRefreshToken(member, token.getRefreshToken());

//            response.setContentType("application/json;charset=UTF-8");
            Boolean isLocal = CookieUtil.getCookie(request, IS_LOCAL)
                    .map(Cookie::getValue)
                    .map(Boolean::parseBoolean).orElse(false);


            oAuth2SuccessHandler.writeTokenResponse(request, response, token);
            String targetUrl = oAuth2SuccessHandler.determineTargetUrlDelegate(request, response, token, isLocal);
            Map<String, String> map = new HashMap<>();
            map.put("redirectUrl", targetUrl);
            return ResponseEntity.ok(map);
        }

        throw new MemberException(ErrorCode.MEMBER_NOT_FOUND);
    }

}
