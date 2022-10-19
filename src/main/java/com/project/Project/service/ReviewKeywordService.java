package com.project.Project.service;

import com.project.Project.domain.enums.DTypeEnum;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.domain.review.ReviewKeyword;

import java.util.List;
import java.util.Optional;

public interface ReviewKeywordService {

    Optional<ReviewKeyword> getReviewKeyword(KeywordEnum type, DTypeEnum dType);

    List<ReviewKeyword> getReviewKeywordByType(KeywordEnum type);

    List<ReviewKeyword> getReviewKeywordByDType(DTypeEnum dType);

    List<ReviewKeyword> initReviewKeyword();

    List<ReviewKeyword> getReviewCategoryList();
}
