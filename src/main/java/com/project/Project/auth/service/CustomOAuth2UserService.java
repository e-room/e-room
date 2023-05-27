package com.project.Project.auth.service;

import com.project.Project.auth.dto.OAuthAttributes;
import com.project.Project.auth.enums.MemberRole;
import com.project.Project.domain.member.Member;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.repository.member.RoleRepository;
import com.project.Project.service.member.ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final ProfileImageService profileImageService;
    private final RoleRepository roleRepository;
    private final WebClient nickNameWebClient;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Map<String, Object> modifiableAttributes = new HashMap<>(attributes.getAttributes());
        modifiableAttributes.put("registrationId", registrationId);
        modifiableAttributes.put("userNameAttributeName", userNameAttributeName);
        Member user = saveOrUpdate(attributes);

        return new DefaultOAuth2User(
                user.getRoles(),
                modifiableAttributes,
                attributes.getNameAttributeKey());
    }

    private Member newMember(OAuthAttributes attributes) {
        String nickName = nickNameWebClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("format", "text").queryParam("max_length", 8).build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Member newMember = attributes.toEntity();
        newMember.setProfileImage(profileImageService.random());
        newMember.setNickName(nickName);
        newMember.setRoles(Arrays.asList(MemberRole.USER), this.roleRepository);
        return newMember;
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmailAndAuthProviderType(attributes.getEmail(), attributes.getAuthProviderType())
                .map(entity -> entity.update(attributes.getName()))
                .orElseGet(() -> newMember(attributes));

        return memberRepository.save(member);
    }
}