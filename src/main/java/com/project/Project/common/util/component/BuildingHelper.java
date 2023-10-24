package com.project.Project.common.util.component;

import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.building.BuildingException;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.service.building.BuildingGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BuildingHelper {

    private final BuildingCustomRepository buildingCustomRepository;
    private final BuildingRepository buildingRepository;

    /**
     * 도로명 주소로 검색된 건물 목록이 DB에 존재하면 반환, 그렇지 않으면 생성해주고 반환해줍니다.
     * @param roadNameAndBuildingNumber 도로명 주소 문자열입니다. ex) 매영로425번길 4
     * @param cursorIds
     * @param page
     * @return 검색된 빌딩 목록
     */
    public List<Building> searchOrCreateBuildings(String roadNameAndBuildingNumber, List<Double> cursorIds, Pageable page) {
        return retrieveOrGenerateBuildings(roadNameAndBuildingNumber, cursorIds, page);
    }

    public List<Building> searchOrCreateBuildings(String roadNameAndBuildingNumber) {
        List<Double> cursorIds = new ArrayList<>();
        Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        return retrieveOrGenerateBuildings(roadNameAndBuildingNumber, cursorIds, page);
    }

    public Building searchOrCreateBuilding(Address address) {
        try {
            return buildingRepository.findBuildingByAddress(address)
                    .orElseGet(() -> BuildingGenerator.generateBuilding(address));
        } catch (DataIntegrityViolationException e) {
            return buildingRepository.findBuildingByAddress(address)
                    .orElseThrow(() -> new BuildingException(ErrorCode.ADDRESS_BAD_REQUEST));
        } catch (Exception e) {
            throw new BuildingException(ErrorCode.ADDRESS_BAD_REQUEST);
        }
    }

    private List<Building> retrieveOrGenerateBuildings(String roadNameAndBuildingNumber, List<Double> cursorIds, Pageable page) {
        List<Building> buildingList = buildingCustomRepository.searchBuildings(roadNameAndBuildingNumber, cursorIds, page);

        if (buildingList.isEmpty()) {
            try {
                buildingList = BuildingGenerator.generateBuildings(roadNameAndBuildingNumber);
            } catch (DataIntegrityViolationException e) {
                // Unique 제약 조건 위반 시, 해당 주소로 빌딩을 다시 검색합니다.
                buildingList = buildingCustomRepository.searchBuildings(roadNameAndBuildingNumber, cursorIds, page);
            } catch (Exception e) {
                // 예외 응답을 주는 대신, 빈 리스트를 반환합니다.
                buildingList = new ArrayList<>();
            }
        }

        return buildingList;
    }
}
