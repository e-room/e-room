package com.project.Project.validator;

import com.project.Project.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class RoomExistValidator implements ConstraintValidator<ExistRoom, Long> {

    private final RoomRepository roomRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return roomRepository.existsById(value);
    }

    @Override
    public void initialize(ExistRoom constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
