package com.project.Project.service;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.enums.MemberRole;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.interaction.FavoriteRepository;
import com.project.Project.repository.interaction.ReviewLikeRepository;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.member.impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {


    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private MemberServiceImpl memberService;


    @Test
    void deleteMember_Test() {
        // given
        Member member = Member.builder()
                .reviewLikeList(new ArrayList<>())
                .favoriteBuildingList(new ArrayList<>())
                .reviewLikeList(new ArrayList<>())
                .build();

        memberRepository.save(member);

        Review review = Review.builder().author(member).build(); member.getReviewList().add(review);
        ReviewLike reviewLike = ReviewLike.builder().member(member).build(); reviewLike.setMember(member); reviewLike.setReview(review);
        Favorite favorite = Favorite.builder().member(member).building(Building.builder().build()).build(); member.getFavoriteBuildingList().add(favorite);

        reviewRepository.save(review);
        reviewLikeRepository.save(reviewLike);
        favoriteRepository.save(favorite);

        // when
        Long deletedMemberId = memberService.delete(member);

        // then
        assertEquals(member.getId(), deletedMemberId);
        assertEquals(0, memberRepository.count());
    }
}
