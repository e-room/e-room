package com.project.Project.service.impl;

import com.project.Project.Util.BuildingGenerator;
import com.project.Project.Util.KakaoAddressAPI;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.ReviewRepository;
import com.project.Project.repository.RoomRepository;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;
    private final BuildingCustomRepository buildingCustomRepo;
    private final RoomRepository roomRepository;
    private final ReviewRepository reviewRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final WebClient webclient;

    public List<OnlyBuildingIdAndCoord> getAllBuildingsIdAndCoord(){
        return buildingRepository.findBy(OnlyBuildingIdAndCoord.class);
    }

    @Override
    public List<Building> getBuildingListByBuildingIds(List<Long> buildingIds, Long cursorId, Pageable pageable) {
        return buildingRepository.findBuildingsByIdIn(buildingIds);
    }

    @Override
    public Building getBuildingByBuildingId(Long buildingId) {
        return buildingRepository.findBuildingById(buildingId);
    }

    @Override
    public List<Building> getBuildingsBySearch(String params, Long cursorId, Pageable page) {
//        return buildingRepository.searchBuildings(params);
       return  buildingCustomRepo.searchBuildings(params,cursorId,page);
    }

    @Override
    public Optional<Building> findByAddress(Address address) {
        return buildingRepository.findByAddress(address);
    }

    @Override
    public Building createBuilding(String address) { // todo : 구현
        return null;
    }
}
