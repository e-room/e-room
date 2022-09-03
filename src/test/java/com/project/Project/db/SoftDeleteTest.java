package com.project.Project.db;

import com.project.Project.repository.ReviewFormRepository;
import com.project.Project.domain.review.ReviewForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
public class SoftDeleteTest {

    ReviewFormRepository reviewFormRepository;

    @Autowired
    public SoftDeleteTest(ReviewFormRepository reviewFormRepository) {
        this.reviewFormRepository = reviewFormRepository;
    }

    @Transactional
    @Test
    @Rollback(value = false)
    void softDelete() {
        // given
        ReviewForm firstReviewForm = reviewFormRepository.save(new ReviewForm());
        ReviewForm secondReviewForm = reviewFormRepository.save(new ReviewForm());

        // when
        reviewFormRepository.delete(firstReviewForm);

        // then
        assertThat(reviewFormRepository.findAll().size()).isEqualTo(1);
    }
}
