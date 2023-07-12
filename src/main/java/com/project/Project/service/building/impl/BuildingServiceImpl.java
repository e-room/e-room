package com.project.Project.service.building.impl;

import com.project.Project.controller.building.dto.BuildingOptionalDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.service.building.BuildingGenerator;
import com.project.Project.service.building.BuildingService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final BuildingCustomRepository buildingCustomRepo;

    public List<OnlyBuildingIdAndCoord> getBuildingMarking() {
        return buildingCustomRepo.getBuildingMarking();
    }

    @Override
    public List<Building> getBuildingListByBuildingIds(List<Long> buildingIds, List<Double> cursorIds, Pageable pageable) {
        List<Building> buildingList = buildingCustomRepo.findBuildingsByIdIn(buildingIds, cursorIds, pageable);
        buildingList.stream().map(Building::getBuildingSummary).forEach(Hibernate::initialize);
        buildingList.stream().map(Building::getBuildingToReviewCategoryList).forEach(Hibernate::initialize);

        return buildingList;
    }

    @Override
    public Optional<Building> getBuildingByBuildingId(Long buildingId) {
        return Optional.ofNullable(buildingRepository.findBuildingById(buildingId));
    }

    @Transactional
    @Override
    public List<Building> getBuildingsBySearch(String params, List<Double> cursorIds, Pageable page) {
        List<Building> buildingList;
        buildingList = buildingCustomRepo.searchBuildings(params, cursorIds, page);
        if(buildingList.isEmpty()) {
            try {
                buildingList = createBuilding(params);
            } catch (DataIntegrityViolationException e) {
                // Unique 제약 조건 위반 시, 해당 주소로 빌딩을 다시 검색합니다. (동시에 DB에 없는 같은 주소를 검색한 경우)
                buildingList = buildingCustomRepo.searchBuildings(params, cursorIds, page);
            } catch (Exception e) {
                // 예외 응답을 주는 대신, 빈 리스트를 반환합니다. (BuildingException, ...)
                buildingList = new ArrayList<>();
            }
        }
        return buildingList;
    }

    @Override
    public Optional<Building> findByAddress(Address address) {
        return buildingRepository.findByAddress(address);
    }

    @Override
    @Transactional
    public Building createBuilding(Address address, BuildingOptionalDto buildingOptionalDto) {
        Building building = BuildingGenerator.generateBuilding(address);
        return building.setOptions(buildingOptionalDto);
    }

    @Override
    public Building createBuilding(Address address, Coordinate coordinate, BuildingOptionalDto buildingOptionalDto) {
        Building building = BuildingGenerator.generateBuilding(address, coordinate);
        return building.setOptions(buildingOptionalDto);
    }

    @Override
    public List<Building> createBuilding(String address) {
        return BuildingGenerator.generateBuildings(address);
    }

    public Building updateBuilding(BuildingOptionalDto buildingOptionalDto) {
        return null;
    }
}
