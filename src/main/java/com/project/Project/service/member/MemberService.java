package com.project.Project.service.member;

import com.project.Project.controller.building.dto.CoordinateDto;
import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.member.RecentMapLocation;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Optional<Member> findByEmailAndAuthProviderType(String email, AuthProviderType authProviderType);

    RecentMapLocation updateRecentMapLocation(CoordinateDto coordinateDto, Member member);

    Optional<Member> findById(Long id);

    public Integer getReviewCnt(Member member);

    public List<ReviewLike> getReviewLikeList(Member member);

    Long delete(Member member);
}
