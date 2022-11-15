package com.project.Project;

import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class ProjectApplication {

    private final ReviewCategoryService reviewCategoryService;
    private final ReviewKeywordService reviewKeywordService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @PostConstruct()
    public void init() {
        reviewCategoryService.initReviewCategory();
        reviewKeywordService.initReviewKeyword();
    }
}