package com.project.Project.validator.interaction;

import com.project.Project.domain.Member;
import com.project.Project.repository.interaction.FavoriteRepository;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class FavoriteExistValidator implements ConstraintValidator<ExistFavorite, FavoriteExistVO> {

    private final FavoriteRepository favoriteRepository;

    @Override
    public void initialize(ExistFavorite constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(FavoriteExistVO favoriteExistVO, ConstraintValidatorContext context) {
        return favoriteRepository.existsByMemberAndBuilding_Id(favoriteExistVO.getMember(), favoriteExistVO.getBuildingId());
    }
}
