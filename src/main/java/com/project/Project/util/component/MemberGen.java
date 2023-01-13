package com.project.Project.util.component;

import com.project.Project.auth.dto.Token;
import com.project.Project.auth.service.TokenService;
import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.enums.MemberRole;
import com.project.Project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberGen {
    private final MemberRepository memberRepository;
    private final TokenService tokenService;


    public Member createMember(String name, String email) {
        Token newToken = tokenService.generateToken(email, AuthProviderType.KAKAO,"USER");
        Member member = Member.builder()
                .email(email)
                .name(name)
                .refreshToken(newToken.getRefreshToken())
                .profileImage(null)
                .memberRole(MemberRole.USER)
                .build();
        return memberRepository.save(member);
    }
}
