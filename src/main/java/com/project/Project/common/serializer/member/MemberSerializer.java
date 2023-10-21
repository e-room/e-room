package com.project.Project.common.serializer.member;

import com.project.Project.auth.dto.MemberDto;
import com.project.Project.auth.dto.OAuthAttributes;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.member.MemberException;
import com.project.Project.controller.building.dto.CoordinateDto;
import com.project.Project.controller.member.dto.MemberResponseDto;
import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.member.RecentMapLocation;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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

    public static MemberDto toAuthorDto(Review review) {
        if (Boolean.TRUE.equals(review.getIsAnonymous())) return MemberDto.builder().id(review.getAuthor().getId()).name(review.getAuthor().getNickName()).email(null).picture(review.getAuthor().getProfileImage().getUrl()).build();
        return toDto(review.getAuthor());
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
                .authProviderType(oAuthAttributes.getAuthProviderType())
                .build();
    }

    public static Member toMember(MemberDto memberDto) {
        String email = memberDto.getEmail();
        AuthProviderType authProviderType = memberDto.getAuthProviderType();
        Member member = staticMemberRepo.findByEmailAndAuthProviderType(email, authProviderType).orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
        return member;
    }

    public static MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .picture(member.getProfileImage().getUrl())
                .authProviderType(member.getAuthProviderType())
                .build();
    }

    public static MemberResponseDto.MemberProfileDto toMemberProfileDto(Member member) {
        return MemberResponseDto.MemberProfileDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .profileImageUrl(member.getProfileImage().getUrl())
                .build();
    }

    public static MemberResponseDto.MemberDeleteDto toMemberDeleteDto(Long deletedMemberId) {
        return MemberResponseDto.MemberDeleteDto.builder()
                .memberId(deletedMemberId)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static MemberResponseDto.RecentMapLocationDto toRecentMapLocationDto(RecentMapLocation recentMapLocation) {
        return MemberResponseDto.RecentMapLocationDto.builder()
                .updatedAt(LocalDateTime.now())
                .coordinateDto(
                        CoordinateDto.builder()
                                .latitude(recentMapLocation.getCoordinate().getLatitude())
                                .longitude(recentMapLocation.getCoordinate().getLongitude())
                                .build()
                )
                .build();
    }
}
