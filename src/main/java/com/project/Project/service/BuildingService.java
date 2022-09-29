package com.project.Project.service;

import com.project.Project.domain.building.Building;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BuildingService {
    List<Building> getBuildingListByBuildingIds(List<Long> buildingIds, Long cursorId, Pageable page);
    Building getBuildingByBuildingId(Long buildingId);
    List<Building> getBuildingBySearch(String params, Long cursorId, Pageable page);
    Optional<Building> findByAddress(String address);
    List<OnlyBuildingIdAndCoord> getAllBuildingsIdAndCoord();
    Building createBuilding(String address);
}