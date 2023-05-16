package com.project.Project.serializer.admin;

import com.project.Project.auth.dto.Token;
import com.project.Project.auth.enums.MemberRole;
import com.project.Project.auth.service.TokenService;
import com.project.Project.bootstrap.ProfileImageGenerator;
import com.project.Project.controller.admin.dto.AdminRequestDto;
import com.project.Project.controller.admin.dto.AdminResponseDto;
import com.project.Project.domain.auth.Role;
import com.project.Project.domain.member.Member;
import com.project.Project.service.member.ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AdminSerializer {

    private final ProfileImageService profileImageService;
    private final TokenService tokenService;

    // static context
    private static ProfileImageService staticProfileImageService;
    private static TokenService staticTokenService;

    @PostConstruct
    void init() {
        staticProfileImageService = this.profileImageService;
        staticTokenService = this.tokenService;
    }

    public static AdminResponseDto.CreateMemberDto toCreateMemberDto(Member createdMember) {
        return AdminResponseDto.CreateMemberDto.builder()
                .memberId(createdMember.getId())
                .createdAt(createdMember.getCreatedAt())
                .build();
    }

    public static Member toMember(AdminRequestDto.CreateMemberDto request) {
        Token newToken = staticTokenService.generateToken(request.getEmail(), request.getAuthProviderType(), MemberRole.USER);

        Member member = Member.builder()
                .authProviderType(request.getAuthProviderType())
                .email(request.getEmail())
                .name(request.getName())
                .refreshToken(newToken.getRefreshToken())
                .favoriteBuildingList(new ArrayList<>())
                .reviewList(new ArrayList<>())
                .favoriteBuildingList(new ArrayList<>())
                .reviewLikeList(new ArrayList<>())
                .roles(Arrays.asList(Role.builder().memberRole(MemberRole.USER).build()))
                .profileImage(staticProfileImageService.random())
                .recentMapLocation(null)
                .build();

        return member;
    }
}
