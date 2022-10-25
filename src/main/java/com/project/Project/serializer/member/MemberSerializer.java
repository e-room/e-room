package com.project.Project.serializer.member;

import com.project.Project.auth.dto.MemberDto;
import com.project.Project.domain.Member;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class MemberSerializer {

    public static MemberDto toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return MemberDto.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .build();
    }

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .email((String) member.getEmail())
                .name((String) member.getName())
                .picture(member.getProfileImageUrl())
                .build();
    }
}
