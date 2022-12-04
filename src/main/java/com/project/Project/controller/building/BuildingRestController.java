package com.project.Project.controller.building;

import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.building.dto.BuildingRequestDto;
import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.serializer.building.BuildingSerializer;
import com.project.Project.service.building.BuildingService;
import com.project.Project.service.review.ReviewService;
import com.project.Project.util.component.QueryDslUtil;
import com.project.Project.validator.ExistBuilding;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ResponseEntity<BuildingResponseDto.BuildingCountResponse> getBuildingMarker() {
        List<OnlyBuildingIdAndCoord> buildingList = this.buildingService.getAllBuildingsIdAndCoord();
        BuildingResponseDto.BuildingCountResponse response = BuildingSerializer.toBuildingCountResponse(buildingList);
        return ResponseEntity.ok(response);
    }

    /*
    when: 3.0.2
    request: buildingId list
    return: 해당하는 건물 list
     */
    @GetMapping("")
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> getBuildingList(@RequestParam List<@ExistBuilding Long> buildingIds, @RequestParam(required = false) List<Double> cursorIds, @PageableDefault(size = 10, sort = {"id", "reviewCnt", "avgScore"}, page = 0, direction = Sort.Direction.DESC) Pageable pageable) {
        if (cursorIds == null) cursorIds = new ArrayList<>();
        List<Building> buildingList = this.buildingService.getBuildingListByBuildingIds(buildingIds, cursorIds, pageable);
        List<BuildingResponseDto.BuildingListResponse> buildingListResponse = buildingList.stream().map((building) -> BuildingSerializer.toBuildingListResponse(building)).collect(Collectors.toList());
        return ResponseEntity.ok(QueryDslUtil.toSlice(buildingListResponse, pageable));
    }

    /*
    when: 3.2
    request: buildingId
    return: 단일 건물 BuildingResponse
     */
    @GetMapping("/{buildingId}")
    public ResponseEntity<BuildingResponseDto.BuildingResponse> getBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId) {
        Building building = this.buildingService.getBuildingByBuildingId(buildingId).orElseThrow(() -> new BuildingException(ErrorCode.BUILDING_NOT_FOUND));
        building.getRoomList().stream().forEach(Hibernate::initialize);
        building.getBuildingToReviewCategoryList().stream().forEach(Hibernate::initialize);
        return ResponseEntity.ok(BuildingSerializer.toBuildingResponse(building));
    }

    /* 8.1
    when:
    request: 검색 코드(Enum), searchParams(주소,단일 Or 복수, 집 주소 등등)
    service:
        - 검색 코드를 바탕으로 동적으로 각자 다른 클래스를 호출하도록 동적으로 처리
    return: 건물 정보
     */
    @GetMapping("/search")
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> searchBuilding(@RequestParam("params") String params, @RequestParam(required = false) List<Double> cursorIds, @PageableDefault(size = 10, sort = "id", page = 0, direction = Sort.Direction.DESC) Pageable pageable) {
        if (cursorIds == null) cursorIds = new ArrayList<>();
        List<Building> buildingList = this.buildingService.getBuildingsBySearch(params, cursorIds, pageable);
        List<BuildingResponseDto.BuildingListResponse> buildingListResponse = buildingList.stream().map((building) -> BuildingSerializer.toBuildingListResponse(building)).collect(Collectors.toList());
        return ResponseEntity.ok(QueryDslUtil.toSlice(buildingListResponse, pageable));
    }

    /*
    building set generator for test
     */
    @PostMapping("/")
    public ResponseEntity<BuildingResponseDto.BuildingMetaData> createBuilding(@RequestBody BuildingRequestDto.BuildingCreateRequest request) {
        Building building = this.buildingService.createBuilding(AddressDto.toAddress(request.getAddressDto()), request.getBuildingOptionalDto());
        return ResponseEntity.ok(BuildingSerializer.toBuildingMetaData(building));
    }
}
