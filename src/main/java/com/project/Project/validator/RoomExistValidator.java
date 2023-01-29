package com.project.Project.validator;

import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.room.RoomException;
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
        if(!roomRepository.existsById(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.ROOM_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(ExistRoom constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
