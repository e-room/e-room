package com.project.Project;

import com.project.Project.domain.review.ReviewCategory;
import com.project.Project.repository.ReviewCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class ProjectApplication {

    private final ReviewCategoryRepository reviewCategoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @PostConstruct()
    public void init() {
        ReviewCategory.init(reviewCategoryRepository);
    }
}