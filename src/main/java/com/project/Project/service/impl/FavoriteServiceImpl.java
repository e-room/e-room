package com.project.Project.service.impl;

import com.project.Project.domain.Member;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.interaction.FavoriteRepository;
import com.project.Project.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final BuildingRepository buildingRepository;
    private final FavoriteRepository favoriteRepository;


    public Long addFavoriteBuilding(Long buildingId, Member member) {
        Building building = buildingRepository.findBuildingById(buildingId);
        Favorite favorite = Favorite.builder().build();
        favorite.setBuilding(building);
        favorite.setMember(member);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return savedFavorite.getId();
    }
}
