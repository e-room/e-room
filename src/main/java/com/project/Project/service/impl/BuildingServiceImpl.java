package com.project.Project.service.impl;

import com.project.Project.domain.building.Building;
import com.project.Project.repository.BuildingRepository;
import com.project.Project.repository.ReviewRepository;
import com.project.Project.repository.RoomRepository;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;
    private final RoomRepository roomRepository;
    private final ReviewRepository reviewRepository;


    public List<OnlyBuildingIdAndCoord> getAllBuildingsIdAndCoord(){
        return buildingRepository.findBy(OnlyBuildingIdAndCoord.class);
    }

    @Override
    public List<Building> getBuildingListByBuildingIds(List<Long> buildingIds) {
        return buildingRepository.findBuildingsByIdIn(buildingIds);
    }

    @Override
    public Building getBuildingByBuildingId(Long buildingId) {
        return buildingRepository.findBuildingById(buildingId);
    }

    @Override
    public List<Building> getBuildingBySearch(String params) {
        return buildingRepository.searchBuildings(params);
    }

    @Override
    public Optional<Building> findByAddress(String address) {
        return buildingRepository.findByAddress(address);
    }

    @Override
    public Building createBuilding(String address) { // todo : 구현
        return null;
    }
}
