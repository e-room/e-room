package com.project.Project.service.member.impl;

import com.project.Project.controller.building.dto.CoordinateDto;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.enums.ReviewLikeStatus;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.member.RecentMapLocation;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.interaction.FavoriteRepository;
import com.project.Project.repository.interaction.ReviewLikeRepository;
import com.project.Project.repository.member.MemberCustomRepository;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.repository.review.ReviewCustomRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberCustomRepository memberCustomRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewCustomRepository reviewCustomRepository;

    @Transactional
    public RecentMapLocation updateRecentMapLocation(CoordinateDto coordinateDto, Member member) {
        Coordinate coordinate = Coordinate.builder()
                .latitude(coordinateDto.getLatitude())
                .longitude(coordinateDto.getLongitude())
                .build();

        RecentMapLocation recentMapLocation;
        if (member.getRecentMapLocation() == null) {
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


    public Optional<Member> findByEmailAndAuthProviderType(String email, AuthProviderType authProviderType) {
        return memberRepository.findByEmailAndAuthProviderType(email, authProviderType);
    }

    public Optional<Member> findById(Long id) {
        return this.memberRepository.findById(id);
    }

    @Transactional
    public Long delete(Member member) {
        Long memberId = member.getId();
        memberRepository.deleteById(memberId);
        return memberId;
    }

    @Override
    public Integer getReviewCnt(Member member) {
        Long count = memberCustomRepository.getReviewCnt(member);
        return Math.toIntExact(count);
    }

    @Override
    public List<ReviewLike> getReviewLikeList(Member member) {
        return reviewLikeRepository.findByReviewLikeStatusAndMember(ReviewLikeStatus.LIKED, member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public void updateRefreshToken(Member member, String refreshToken) {
        member.setRefreshToken(refreshToken);
    }
}
