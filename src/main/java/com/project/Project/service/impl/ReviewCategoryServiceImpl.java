package com.project.Project.service.impl;

import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.ReviewCategory;
import com.project.Project.repository.ReviewCategoryRepository;
import com.project.Project.service.ReviewCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewCategoryServiceImpl implements ReviewCategoryService {

    private final ReviewCategoryRepository reviewCategoryRepository;

    @Override
    public Optional<ReviewCategory> getReviewCategory(ReviewCategoryEnum type) {
        return reviewCategoryRepository.findByType(type);
    }

    @Override
    public List<ReviewCategory> initReviewCategory() {
        ReviewCategory.init(reviewCategoryRepository);
        return reviewCategoryRepository.findAll();
    }

    @Override
    public List<ReviewCategory> getReviewCategoryList() {
        return reviewCategoryRepository.findAll();
    }
}
