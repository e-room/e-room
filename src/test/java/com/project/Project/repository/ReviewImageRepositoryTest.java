package com.project.Project.repository;

import com.project.Project.domain.Uuid;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.repository.review.ReviewImageRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewKeywordService;
import com.project.Project.unit.repository.RepositoryTestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewKeywordService.class, ReviewCategoryService.class}))
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
public class ReviewImageRepositoryTest {
    @Autowired
    private ReviewImageRepository reviewImageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    Review review;
    ReviewImage reviewImage;

    @BeforeEach
    void setup() {
        review = Review.builder()
                .reviewImageList(new ArrayList<>())
                .build();
        reviewRepository.save(review);

        reviewImage = ReviewImage.builder()
                .uuid(Uuid.builder().uuid("iloveeroom").build())
                .url("https://d2ykyi5jl9muoc.cloudfront.net/profile-images/blue-smile_eyes-d_mouth.png")
                .review(review)
                .build();

        reviewImageRepository.save(reviewImage);
    }

    @AfterEach
    void cleanup() {
        reviewImageRepository.deleteAll();
        reviewRepository.deleteAll();
    }

    @Test
    void existsByUuid_Test() {
        boolean expected = true;
        boolean actual = reviewImageRepository.existsByUuid("iloveeroom");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByUuid_Test() {
        ReviewImage expected = reviewImage;
        ReviewImage actual = reviewImageRepository.findByUuid("iloveeroom").get();

        assertThat(actual)
                .usingRecursiveComparison().isEqualTo(expected);
    }
}

