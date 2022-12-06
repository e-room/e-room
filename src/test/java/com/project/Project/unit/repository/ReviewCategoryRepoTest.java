package com.project.Project.unit.repository;

import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.ReviewCategory;
import com.project.Project.repository.review.ReviewCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
public class ReviewCategoryRepoTest {


    private ReviewCategoryRepository reviewCategoryRepository;

    @Autowired
    public ReviewCategoryRepoTest(ReviewCategoryRepository reviewCategoryRepository) {
        this.reviewCategoryRepository = reviewCategoryRepository;
    }

    @BeforeEach
    public void setup() {
        ReviewCategory.init(reviewCategoryRepository);
    }

    @Test
    public void findByTypeTest() {
        ReviewCategoryEnum searchedEnum = reviewCategoryRepository.findByType(ReviewCategoryEnum.BUILDINGCOMPLEX)
                .orElseThrow(() -> new RuntimeException("해당하는 enum이 없습니다."))
                .getType();
        assertThat(searchedEnum).isEqualTo(ReviewCategoryEnum.BUILDINGCOMPLEX);
    }

}
