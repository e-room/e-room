package com.project.Project.common.util.component;

import com.project.Project.auth.dto.Token;
import com.project.Project.auth.enums.MemberRole;
import com.project.Project.auth.service.TokenService;
import com.project.Project.domain.auth.Role;
import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.member.Member;
import com.project.Project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class MemberGen {
    private final MemberRepository memberRepository;
    private final TokenService tokenService;


    public Member createMember(String name, String email) {
        Token newToken = tokenService.generateToken(email, AuthProviderType.KAKAO, MemberRole.USER);
        Member member = Member.builder()
                .email(email)
                .name(name)
                .refreshToken(newToken.getRefreshToken())
                .profileImage(null)
                .roles(Arrays.asList(Role.builder().memberRole(MemberRole.USER).build()))
                .build();
        return memberRepository.save(member);
    }
}
