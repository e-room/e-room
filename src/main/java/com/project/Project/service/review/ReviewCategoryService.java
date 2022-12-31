package com.project.Project.service.review;

import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.ReviewCategory;

import java.util.List;
import java.util.Optional;

public interface ReviewCategoryService {

    Optional<ReviewCategory> getReviewCategory(ReviewCategoryEnum type);

    List<ReviewCategory> initReviewCategory();

    List<ReviewCategory> getReviewCategoryList();
}
