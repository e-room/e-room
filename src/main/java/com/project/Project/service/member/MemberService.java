package com.project.Project.service.member;

import com.project.Project.controller.building.dto.CoordinateDto;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.member.RecentMapLocation;

import java.util.Optional;

public interface MemberService {

    RecentMapLocation updateRecentMapLocation(CoordinateDto coordinateDto, Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Long delete(Member member);
}
