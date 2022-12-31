package com.project.Project.repository;

import com.project.Project.domain.Member;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.interaction.ReviewLikeRepository;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewKeywordService;
import com.project.Project.unit.repository.RepositoryTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewKeywordService.class, ReviewCategoryService.class}))
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
public class ReviewLikeRepositoryTest {
    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    ReviewLike reviewLike;
    Member member;
    Review review;

    @BeforeEach
    void setup() {
        member = Member.builder()
                .reviewList(new ArrayList<>())
                .favoriteBuildingList(new ArrayList<>())
                .reviewLikeList(new ArrayList<>())
                .build();
        memberRepository.save(member);

        review = Review.builder()
                .reviewLikeList(new ArrayList<>())
                .reviewToReviewCategoryList(new ArrayList<>())
                .reviewImageList(new ArrayList<>())
                .reviewToReviewKeywordList(new ArrayList<>())
                .likeMemberList(new ArrayList<>())
                .build();
        reviewRepository.save(review);


        reviewLike = ReviewLike.builder().build();
        reviewLike.setReview(review);
        reviewLike.setMember(member);


        reviewLikeRepository.save(reviewLike);
    }

    @Test
    void findByMemberAndReview_Id_Test() {
        ReviewLike expected = reviewLike;
        ReviewLike actual = reviewLikeRepository.findByMemberAndReview_Id(member, review.getId()).get();

        assertThat(actual)
                .usingRecursiveComparison().isEqualTo(expected);
    }
}
