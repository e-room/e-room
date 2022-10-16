package com.project.Project.service;

import com.project.Project.controller.building.dto.BuildingOptionalDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BuildingService {
    List<Building> getBuildingListByBuildingIds(List<Long> buildingIds, Long cursorId, Pageable page);
    Building getBuildingByBuildingId(Long buildingId);
    List<Building> getBuildingsBySearch(String params, Long cursorId, Pageable page);
    Optional<Building> findByAddress(Address address);
    List<OnlyBuildingIdAndCoord> getAllBuildingsIdAndCoord();
    Building createBuilding(Address address, BuildingOptionalDto buildingOptionalDto);

    Building createBuilding(Address address, Coordinate coordinate, BuildingOptionalDto buildingOptionalDto);
    List<Building> createBuilding(String address);

}