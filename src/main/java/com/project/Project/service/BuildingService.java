package com.project.Project.service;

import com.project.Project.domain.building.Building;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;

import java.util.List;
import java.util.Optional;

public interface BuildingService {
    List<Building> getBuildingListByBuildingIds(List<Long> buildingIds);
    Building getBuildingByBuildingId(Long buildingId);
    List<Building> getBuildingBySearch(String params);
    Optional<Building> findByAddress(String address);
    List<OnlyBuildingIdAndCoord> getAllBuildingsIdAndCoord();
    Building createBuilding(String address);
}