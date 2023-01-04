package com.project.Project.service.impl;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.domain.member.Member;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.interaction.FavoriteRepository;
import com.project.Project.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {


    private final BuildingRepository buildingRepository;
    private final FavoriteRepository favoriteRepository;
    private final BuildingCustomRepository buildingCustomRepo;

    @Transactional
    public Long deleteFavoriteBuilding(Long buildingId, Member member) {
        Building building = buildingRepository.findBuildingById(buildingId);
        Favorite favorite = favoriteRepository.findByBuildingAndMember(building, member);
        favorite.deleteMemberAndBuilding();
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

    public List<Building> getBuildingListByMember(Member member, List<Double> cursorIds, Pageable pageable) {
        List<Favorite> favoriteList = favoriteRepository.findByMember(member);
        List<Long> buildingIds = favoriteList.stream()
                .map(favorite -> favorite.getBuilding().getId())
                .collect(Collectors.toList());
        List<Building> buildingList = buildingCustomRepo.findBuildingsByIdIn(buildingIds, cursorIds, pageable);
        buildingList.stream().map(Building::getRoomList).forEach(Hibernate::initialize);
        buildingList.stream().map(Building::getBuildingSummary).forEach(Hibernate::initialize);
        buildingList.stream().map(Building::getBuildingToReviewCategoryList).forEach(Hibernate::initialize);

        return buildingList;
    }
}
