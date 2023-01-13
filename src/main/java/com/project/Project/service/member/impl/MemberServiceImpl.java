package com.project.Project.service.member.impl;

import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.controller.building.dto.CoordinateDto;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.member.RecentMapLocation;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.interaction.FavoriteRepository;
import com.project.Project.repository.interaction.ReviewLikeRepository;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.service.member.MemberService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public RecentMapLocation updateRecentMapLocation(CoordinateDto coordinateDto, Member member) {
        Coordinate coordinate = Coordinate.builder()
                .latitude(coordinateDto.getLatitude())
                .longitude(coordinateDto.getLongitude())
                .build();

        RecentMapLocation recentMapLocation;
        if(member.getRecentMapLocation() == null) {
            recentMapLocation = RecentMapLocation.builder()
                    .coordinate(coordinate)
                    .build();
            member.setRecentMapLocation(recentMapLocation);
        } else {
            recentMapLocation = member.getRecentMapLocation();
            recentMapLocation.setCoordinate(coordinate);
        }
        return recentMapLocation;
    }

    private final ReviewLikeRepository reviewLikeRepository;
    public Optional<Member> findByEmailAndAuthProviderType(String email, AuthProviderType authProviderType) {
        return memberRepository.findByEmailAndAuthProviderType(email, authProviderType);
    }

    public Optional<Member> findById(Long id) {
        return this.memberRepository.findById(id);
    }

    @Transactional
    public Long delete(Member member) {
        /*
        * 연관관계 끊기 -> 멤버 삭제
        * Review : 연관관계만 끊어서 남겨둠
        * Favorite(찜한방) : Hard delete
        * ReviewLike : Hard delete
        * */
        for(Review review : member.getReviewList()) review.deleteAuthor();
        for(Favorite favorite : member.getFavoriteBuildingList()) favorite.deleteMemberAndBuilding();
        favoriteRepository.deleteByMember(member);
        for(ReviewLike reviewLike : member.getReviewLikeList()) reviewLike.deleteMemberAndReview();
        reviewLikeRepository.deleteByMember(member);

        memberRepository.deleteById(member.getId());
        return member.getId();
    }
}
