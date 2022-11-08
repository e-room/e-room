package com.project.Project.service.impl;

import com.project.Project.domain.Member;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.interaction.FavoriteRepository;
import com.project.Project.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final BuildingRepository buildingRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public Long deleteFavoriteBuilding(Long buildingId, Member member) {
        Building building = buildingRepository.findBuildingById(buildingId);
        Favorite favorite = favoriteRepository.findByBuildingAndMember(building, member);
        favoriteRepository.delete(favorite);
        return favorite.getId();
    }

    @Transactional
    public Long addFavoriteBuilding(Long buildingId, Member member) {
        Building building = buildingRepository.findBuildingById(buildingId);
        Favorite favorite = Favorite.builder().build();
        favorite.setBuilding(building);
        favorite.setMember(member);
        Favorite savedFavorite = favoriteRepository.save(favorite);
        return savedFavorite.getId();
    }
}
