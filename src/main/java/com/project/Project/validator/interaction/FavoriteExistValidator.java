package com.project.Project.validator.interaction;

import com.project.Project.domain.member.Member;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.repository.interaction.FavoriteRepository;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoriteExistValidator {

    private final FavoriteRepository favoriteRepository;

    public boolean isValid(Member member, Long buildingId) {
        return favoriteRepository.existsByMemberAndBuilding_Id(member, buildingId);
    }
}
