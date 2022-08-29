package com.project.Project.service;

import com.project.Project.domain.building.Building;

import java.util.List;

public interface BuildingService {
    List<Building> getBuildingListByBuildingIds(List<Long> buildingIds);
    Building getBuildingByBuildingId(Long buildingId);
    List<Building> getBuildingBySearch(String params);
}
