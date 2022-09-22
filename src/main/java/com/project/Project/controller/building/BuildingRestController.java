package com.project.Project.controller.building;

import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewForm;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.service.BuildingService;
import com.project.Project.service.ReviewService;
import com.project.Project.validator.ExistBuilding;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/building")
public class BuildingRestController {

    private final BuildingService buildingService;

    private final ReviewService reviewService;

    /*
    when: 3.0.1
    request: none
    service
        - 좌표 범위 내에 있는 건물 list를 뽑음(Model)
        - 건물 list를 바탕으로 response 객체를 만들어서 전달.
    return: buildingId와 위치를 return
     */
    @GetMapping("/marking")
    public List<BuildingResponseDto.BuildingCountResponse> getBuildingMarker() {
        List<OnlyBuildingIdAndCoord> buildingList = this.buildingService.getAllBuildingsIdAndCoord();
        return buildingList.stream().map((building) -> BuildingResponseDto.BuildingCountResponse.builder()
                .buildingId(building.getId())
                .coordinateDto(Coordinate.toCoordinateDto(building.getCoordinate()))
                .build()).collect(Collectors.toList());
    }

    /*
    when: 3.0.2
    request: buildingId list
    return: 해당하는 건물 list
     */
    @GetMapping("/")
    public List<BuildingResponseDto.BuildingListResponse> getBuildingList(@RequestParam List<@ExistBuilding Long> buildingIds) {
        List<Building> buildingList = this.buildingService.getBuildingListByBuildingIds(buildingIds);

        return buildingList.stream().map(
                (building) -> {
                    List<Review> reviewListByBuilding = this.reviewService.getReviewListByBuildingId(building.getId());
                    Integer reviewCnt = reviewListByBuilding.size();

                    return BuildingResponseDto.BuildingListResponse.builder()
                            .buildingId(building.getId())
                            .name(building.getBuildingName())
                            .address(Address.toAddressDto(building.getAddress()).toString())
                            .isDirectDeal(false)
                            .reviewCnt(Long.valueOf(reviewCnt))
                            .scoreAvg(building.getBuildingSummary().getAvgScore())
                            .bestCategory(building.getBuildingSummary().getBestCategory())
                            .build();
                }
        ).collect(Collectors.toList());
    }

    /*
    when: 3.2
    request: buildingId
    return: 단일 건물 BuildingResponse
     */
    @GetMapping("/{buildingId}")
    public BuildingResponseDto.BuildingResponse getBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId) {
        Building building = this.buildingService.getBuildingByBuildingId(buildingId);
        return building.toBuildingResponse();
    }

    /* 8.1
    when:
    request: 검색 코드(Enum), searchParams(주소,단일 Or 복수, 집 주소 등등)
    service:
        - 검색 코드를 바탕으로 동적으로 각자 다른 클래스를 호출하도록 동적으로 처리
    return: 건물 정보
     */
    @GetMapping("/search")
    public List<BuildingResponseDto.BuildingResponse> searchBuilding(@RequestParam("params") String params) {
        List<Building> buildingList = this.buildingService.getBuildingBySearch(params);
        return buildingList.stream().parallel()
                .map((building)-> building.toBuildingResponse()).collect(Collectors.toList());
    }
}
