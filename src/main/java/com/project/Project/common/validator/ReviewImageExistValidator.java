package com.project.Project.common.validator;

import com.project.Project.common.exception.ErrorCode;
import com.project.Project.repository.review.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class ReviewImageExistValidator implements ConstraintValidator<ExistReviewImage, String> {

    private final ReviewImageRepository reviewImageRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!reviewImageRepository.existsByUuid(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.UUID_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(ExistReviewImage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
