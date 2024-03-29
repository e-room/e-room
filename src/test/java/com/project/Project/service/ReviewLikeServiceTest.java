package com.project.Project.service;

import com.project.Project.auth.enums.MemberRole;
import com.project.Project.domain.auth.Role;
import com.project.Project.domain.enums.ReviewLikeStatus;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.interaction.ReviewLikeRepository;
import com.project.Project.repository.member.RoleRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.interaction.impl.ReviewLikeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ReviewLikeServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewLikeServiceImpl reviewLikeService;


    private void mockRoleRepository() {
        given(roleRepository.findRoleByMemberRoleIn(any()))
                .willReturn(Arrays.asList(Role.builder()
                        .memberRole(MemberRole.USER)
                        .build()));
    }

    private Member createTestMember() {
        return Member.builder()
                .id(10L)// temp user
                .reviewList(new ArrayList<>())
                .favoriteBuildingList(new ArrayList<>())
                .reviewLikeList(new ArrayList<>())
                .name("하품하는 망아지")
                .email("swa07016@khu.ac.kr")
                .roles(Arrays.asList(Role.builder()
                        .memberRole(MemberRole.USER)
                        .build()))
                .refreshToken("mockingMember")
                // .profileImage("https://lh3.googleusercontent.com/ogw/AOh-ky20QeRrWFPI8l-q3LizWDKqBpsWTIWTcQa_4fh5=s64-c-mo")
                .build();
    }

    // 이미 존재하는 경우
    @Test
    void updateReviewLike_UPDATE_Test() {
        // given
        ReviewLike reviewLike = ReviewLike.builder()
                .id(1L)
                .reviewLikeStatus(ReviewLikeStatus.LIKED)
                .build();
        given(reviewLikeRepository.findByMemberAndReview_Id(any(), any()))
                .willReturn(Optional.of(reviewLike));

        // when
        reviewLikeService.updateReviewLike(20L, createTestMember());

        // then
        assertEquals(reviewLike.getReviewLikeStatus(), ReviewLikeStatus.CANCELED);
    }

    // 존재하지 않는 경우
    @Test
    void updateReviewLike_CREATE_Test() {
        Review review = Review.builder().id(5L).build();
        ReviewLike reviewLike = ReviewLike.builder()
                .id(8L)
                .reviewLikeStatus(ReviewLikeStatus.LIKED)
                .build();
        // given
        given(reviewLikeRepository.findByMemberAndReview_Id(any(), any()))
                .willReturn(Optional.ofNullable(null));
        given(reviewRepository.findById(any()))
                .willReturn(Optional.of(review));
        given(reviewLikeRepository.save(any(ReviewLike.class)))
                .willReturn(reviewLike);

        // when
        ReviewLike actual = reviewLikeService.updateReviewLike(5L, createTestMember());

        // then
        assertNotNull(actual);
    }
}
