package com.project.Project.service.member;

import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.controller.building.dto.CoordinateDto;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.member.RecentMapLocation;

import java.util.Optional;

public interface MemberService {

    RecentMapLocation updateRecentMapLocation(CoordinateDto coordinateDto, Member member);

    Optional<Member> findByEmailAndAuthProviderType(String email, AuthProviderType authProviderType);

    Optional<Member> findById(Long id);

    Long delete(Member member);
}
