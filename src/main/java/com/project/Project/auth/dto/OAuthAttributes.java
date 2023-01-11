package com.project.Project.auth.dto;

import com.project.Project.domain.enums.OAuthProviderType;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private OAuthProviderType oAuthProviderType;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, OAuthProviderType oAuthProviderType) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.oAuthProviderType = oAuthProviderType;
    }

    private void setProfileImage(String randomProfileImageUrl) {
        picture = randomProfileImageUrl;
    }
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) return ofNaver("id", attributes);
        if ("kakao".equals(registrationId)) return ofKakao("id", attributes);
        return ofGoogle(userNameAttributeName, attributes);
        // oAuthAttributes.setProfileImage(ProfileImageSelector.select());
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .oAuthProviderType(OAuthProviderType.GOOGLE)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .oAuthProviderType(OAuthProviderType.NAVER)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");
        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) response.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .oAuthProviderType(OAuthProviderType.KAKAO)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .memberRole(MemberRole.USER)
                .oAuthProviderType(oAuthProviderType)
                .build();
    }
}
