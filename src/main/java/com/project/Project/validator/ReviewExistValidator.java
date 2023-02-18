package com.project.Project.validator;

import com.project.Project.exception.ErrorCode;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewExistValidator implements ConstraintValidator<ExistReview, Long> {

    private final ReviewRepository reviewRepository;

    private static ReviewRepository staticReviewRepository;

    @PostConstruct
    public void init() {
        staticReviewRepository = this.reviewRepository;
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if(!reviewRepository.existsById(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.REVIEW_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(ExistReview constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    public static boolean hasReview(Long memberId, Long reviewId) {
        Optional<Review> review = staticReviewRepository.findReviewByAuthorAndReview(memberId, reviewId);
        if(review.isPresent()) return true;
        return false;
    }
}
