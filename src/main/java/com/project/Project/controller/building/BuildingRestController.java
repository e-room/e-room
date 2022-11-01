package com.project.Project.controller.building;

import com.project.Project.controller.CursorDto;
import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.building.dto.BuildingRequestDto;
import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.serializer.building.BuildingSerializer;
import com.project.Project.service.building.BuildingService;
import com.project.Project.service.review.ReviewService;
import com.project.Project.util.component.QueryDslUtil;
import com.project.Project.validator.ExistBuilding;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<BuildingResponseDto.BuildingCountResponse>> getBuildingMarker() {
        List<OnlyBuildingIdAndCoord> buildingList = this.buildingService.getAllBuildingsIdAndCoord();
        List<BuildingResponseDto.BuildingCountResponse> responseList = buildingList.stream().map(BuildingSerializer::toBuildingCountResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    /*
    when: 3.0.2
    request: buildingId list
    return: 해당하는 건물 list
     */
    @GetMapping("/")
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> getBuildingList(@RequestParam List<@ExistBuilding Long> buildingIds, @RequestBody CursorDto cursorDto) {
        Pageable pageable = PageRequest.of(0, cursorDto.getSize());
        List<Building> buildingList = this.buildingService.getBuildingListByBuildingIds(buildingIds, cursorDto.getCursorId(), PageRequest.of(0, cursorDto.getSize()));
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
        Building building = this.buildingService.getBuildingByBuildingId(buildingId);
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
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> searchBuilding(@RequestParam("params") String params, @RequestBody CursorDto cursorDto) {
        Pageable pageable = PageRequest.of(0, cursorDto.getSize());
//        List<Building> buildingList = this.buildingService.getBuildingBySearch(params, cursorDto.getCursorId(),PageRequest.of(0, cursorDto.getSize()));
        List<Building> buildingList = this.buildingService.getBuildingsBySearch(params, cursorDto.getCursorId(), pageable);
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
