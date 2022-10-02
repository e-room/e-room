package com.project.Project.controller.building;

import com.project.Project.controller.CursorDto;
import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.building.BuildingToReviewCategory;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.serializer.building.BuildingSerializer;
import com.project.Project.service.BuildingService;
import com.project.Project.service.ReviewService;
import com.project.Project.validator.ExistBuilding;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Comparator;
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
    public List<BuildingResponseDto.BuildingListResponse> getBuildingList(@RequestParam List<@ExistBuilding Long> buildingIds, @RequestBody CursorDto cursorDto) {
        List<Building> buildingList = this.buildingService.getBuildingListByBuildingIds(buildingIds, cursorDto.getCursorId(), PageRequest.of(0,cursorDto.getSize()));

        return buildingList.stream().map(
                (building) -> {
                    List<Review> reviewListByBuilding = this.reviewService.getReviewListByBuildingId(building.getId(), cursorDto.getCursorId(), PageRequest.of(0, cursorDto.getSize()));
                    Integer reviewCnt = reviewListByBuilding.size();
                    Double buildingScoreAcc = building.getBuildingToReviewCategoryList().stream().map(buildingToReviewCategory -> buildingToReviewCategory.getAvgScore()).reduce(0D, (a,b)-> a + b);
                    Double buildingScoreAvg = buildingScoreAcc/building.getBuildingToReviewCategoryList().size();

                    BuildingToReviewCategory maxScoreCategory = building.getBuildingToReviewCategoryList().stream().max(Comparator.comparing(BuildingToReviewCategory::getAvgScore)).get();
                    return BuildingResponseDto.BuildingListResponse.builder()
                            .buildingId(building.getId())
                            .name(building.getBuildingName())
                            .address(Address.toAddressDto(building.getAddress()).toString())
                            .isDirectDeal(false)
                            .reviewCnt(Long.valueOf(reviewCnt))
                            .scoreAvg(buildingScoreAvg)
                            .bestCategory(maxScoreCategory.getReviewCategory().getType())
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
        return BuildingSerializer.toBuildingResponse(building);
    }

    /* 8.1
    when:
    request: 검색 코드(Enum), searchParams(주소,단일 Or 복수, 집 주소 등등)
    service:
        - 검색 코드를 바탕으로 동적으로 각자 다른 클래스를 호출하도록 동적으로 처리
    return: 건물 정보
     */
    @GetMapping("/search")
    public List<BuildingResponseDto.BuildingResponse> searchBuilding(@RequestParam("params") String params, @RequestBody CursorDto cursorDto) {
        List<Building> buildingList = this.buildingService.getBuildingBySearch(params, cursorDto.getCursorId(),PageRequest.of(0, cursorDto.getSize()));
        return buildingList.stream().parallel()
                .map(BuildingSerializer::toBuildingResponse).collect(Collectors.toList());
    }
}
