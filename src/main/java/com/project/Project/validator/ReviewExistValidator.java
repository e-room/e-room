package com.project.Project.validator;

import com.project.Project.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ReviewExistValidator implements ConstraintValidator<ExistReview, Long> {

    private final ReviewRepository reviewRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return reviewRepository.existsById(value);
    }

    @Override
    public void initialize(ExistReview constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
