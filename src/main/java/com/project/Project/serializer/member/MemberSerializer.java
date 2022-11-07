package com.project.Project.serializer.member;

import com.project.Project.auth.dto.MemberDto;
import com.project.Project.auth.dto.OAuthAttributes;
import com.project.Project.domain.Member;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.member.MemberException;
import com.project.Project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MemberSerializer {

    private final MemberRepository memberRepository;

    private static MemberRepository staticMemberRepo;

    @PostConstruct
    public void init() {
        this.staticMemberRepo = memberRepository;
    }


    public static MemberDto toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        String registrationId = oAuth2User.getAttribute("registrationId");
        String userNameAttributeName = oAuth2User.getAttribute("userNameAttributeName");
        Map<String, Object> cloned = new HashMap<>(attributes);
        cloned.put("response", attributes);
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, cloned);
        return MemberDto.builder()
                .email(oAuthAttributes.getEmail())
                .name(oAuthAttributes.getName())
                .picture(oAuthAttributes.getPicture())
                .build();
    }

    public static Member toMember(MemberDto memberDto) {
        String email = memberDto.getEmail();
        Member member = staticMemberRepo.findByEmail(email).orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
        return member;
    }

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .email((String) member.getEmail())
                .name((String) member.getName())
                .picture(member.getProfileImageUrl())
                .build();
    }
}
