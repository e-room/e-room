package com.project.Project.validator;

import com.project.Project.repository.review.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ReviewImageExistValidator implements ConstraintValidator<ExistReviewImage, String> {

    private ReviewImageRepository reviewImageRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return reviewImageRepository.existsByUuid(value);
    }

    @Override
    public void initialize(ExistReviewImage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
