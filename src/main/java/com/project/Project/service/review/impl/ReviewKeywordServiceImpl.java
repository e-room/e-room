package com.project.Project.service.review.impl;

import com.project.Project.domain.enums.DTypeEnum;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.domain.review.ReviewKeyword;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.service.review.ReviewKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewKeywordServiceImpl implements ReviewKeywordService {

    private final ReviewKeywordRepository reviewKeywordRepository;

    @Override
    public Optional<ReviewKeyword> getReviewKeyword(KeywordEnum type, DTypeEnum dType) {
        return reviewKeywordRepository.findByTypeAndDType(type, dType);
    }

    @Override
    public List<ReviewKeyword> getReviewKeywordByType(KeywordEnum type) {
        return reviewKeywordRepository.findByType(type);
    }

    @Override
    public List<ReviewKeyword> getReviewKeywordByDType(DTypeEnum dType) {
        return reviewKeywordRepository.findByDType(dType);
    }

    @Override
    public List<ReviewKeyword> initReviewKeyword() {
        ReviewKeyword.init(reviewKeywordRepository);
        return reviewKeywordRepository.findAll();
    }

    @Override
    public List<ReviewKeyword> getReviewCategoryList() {
        return reviewKeywordRepository.findAll();
    }

}
