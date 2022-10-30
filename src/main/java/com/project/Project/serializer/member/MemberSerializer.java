package com.project.Project.serializer.member;

import com.project.Project.auth.dto.MemberDto;
import com.project.Project.auth.dto.OAuthAttributes;
import com.project.Project.domain.Member;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class MemberSerializer {

    public static MemberDto toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        String registrationId = oAuth2User.getAttribute("registrationId");
        String userNameAttributeName = oAuth2User.getAttribute("userNameAttributeName");
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);
        return MemberDto.builder()
                .email(oAuthAttributes.getEmail())
                .name(oAuthAttributes.getName())
                .picture(oAuthAttributes.getPicture())
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
