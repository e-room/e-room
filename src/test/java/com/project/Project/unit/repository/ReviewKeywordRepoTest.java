package com.project.Project.unit.repository;

import com.project.Project.domain.enums.DTypeEnum;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.domain.review.ReviewKeyword;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewKeywordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewKeywordService.class, ReviewCategoryService.class}))
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
public class ReviewKeywordRepoTest {

    private ReviewKeywordRepository reviewKeywordRepository;

    @Autowired
    public ReviewKeywordRepoTest(ReviewKeywordRepository reviewKeywordRepository) {
        this.reviewKeywordRepository = reviewKeywordRepository;
    }

    @BeforeEach
    public void setup() {
        ReviewKeyword.init(reviewKeywordRepository);
    }

    @Test
    public void findByTypeTest() {
        List<ReviewKeyword> searchedReviewKeyword = reviewKeywordRepository.findByType(KeywordEnum.ELEVATOR);
        assertThat(searchedReviewKeyword.size()).isEqualTo(DTypeEnum.values().length);

        List<DTypeEnum> dTypeEnumList = searchedReviewKeyword.stream().map(ReviewKeyword::getDType).collect(Collectors.toList());
        assertThat(dTypeEnumList).containsOnly(DTypeEnum.ADVANTAGE, DTypeEnum.DISADVANTAGE);

        List<KeywordEnum> keywordEnums = searchedReviewKeyword.stream().map(ReviewKeyword::getKeywordType).distinct().collect(Collectors.toList());
        assertThat(keywordEnums).containsOnly(KeywordEnum.ELEVATOR);
    }

    @Test
    public void findByDTypeTest() {
        List<ReviewKeyword> searchedReviewKeyword = reviewKeywordRepository.findByDType(DTypeEnum.ADVANTAGE);
        assertThat(searchedReviewKeyword.size()).isEqualTo(KeywordEnum.values().length);
    }

    @Test
    public void findByTypeAndDTypeTest() {
        ReviewKeyword searchedReviewKeyword = reviewKeywordRepository.findByTypeAndDType(KeywordEnum.ELEVATOR, DTypeEnum.ADVANTAGE)
                .orElseThrow(() -> new RuntimeException("해당하는 ReviewKeyword가 없습니다."));
        assertThat(searchedReviewKeyword.getKeywordType()).isEqualTo(KeywordEnum.ELEVATOR);
        assertThat(searchedReviewKeyword.getDType()).isEqualTo(DTypeEnum.ADVANTAGE);

    }
}
