package com.project.Project.controller.building;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.building.dto.BuildingRequestDto;
import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.serializer.building.BuildingSerializer;
import com.project.Project.service.FavoriteService;
import com.project.Project.service.building.BuildingGenerator;
import com.project.Project.service.building.BuildingService;
import com.project.Project.service.review.ReviewImageService;
import com.project.Project.service.review.ReviewService;
import com.project.Project.util.component.QueryDslUtil;
import com.project.Project.validator.ExistBuilding;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Building API", description = "건물 조회, 추가")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/building")
public class BuildingRestController {

    private final BuildingService buildingService;

    private final ReviewService reviewService;

    private final ReviewImageService reviewImageService;
    private final FavoriteService favoriteService;

    /*
    when: 3.0.1
    request: none
    service
        - 좌표 범위 내에 있는 건물 list를 뽑음(Model)
        - 건물 list를 바탕으로 response 객체를 만들어서 전달.
    return: buildingId와 위치를 return
     */
    @Operation(summary = "지도 마킹을 위한 건물 목록 [3.0.1]", description = "건물들의 좌표 목록 조회 API")
    @GetMapping("/marking")
    public ResponseEntity<BuildingResponseDto.BuildingCountResponse> getBuildingMarker() {
        List<OnlyBuildingIdAndCoord> buildingList = this.buildingService.getBuildingMarking();
        BuildingResponseDto.BuildingCountResponse response = BuildingSerializer.toBuildingCountResponse(buildingList);
        return ResponseEntity.ok(response);
    }

    /*
    when: 3.0.2
    request: buildingId list
    return: 해당하는 건물 list
     */
    @Operation(summary = "건물 목록 조회 by buildingId 리스트 [3.0.2]", description = "buildingId의 리스트로 건물들을 조회하는 API")
    @Parameters({
            @Parameter(name = "buildingIds", description = "buildingId 리스트", example = "4126,4128,4130,4132,4134"),
            @Parameter(name = "cursorIds", description = "커서 id", example = "2.4,8714"),
            @Parameter(name = "size", description = "응답 건물 개수", example = "4"),
            @Parameter(name = "sort", description = "정렬 기준", example = "avgScore,id,DESC"),
            @Parameter(name = "pageable", hidden = true)
    })
    @GetMapping("")
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> getBuildingList(@RequestParam List<Long> buildingIds, @RequestParam(required = false) List<Double> cursorIds, @PageableDefault(size = 10, sort = {"id", "reviewCnt", "avgScore"}, page = 0, direction = Sort.Direction.DESC) Pageable pageable) {
        if (cursorIds == null) cursorIds = new ArrayList<>();
        List<Building> buildingList = this.buildingService.getBuildingListByBuildingIds(buildingIds, cursorIds, pageable);
        List<BuildingResponseDto.BuildingListResponse> buildingListResponse = buildingList.stream().map(BuildingSerializer::toBuildingListResponse).collect(Collectors.toList());
        return ResponseEntity.ok(QueryDslUtil.toSlice(buildingListResponse, pageable));
    }

    /*
    when: 3.2
    request: buildingId
    return: 단일 건물 BuildingResponse
     */
    @Operation(summary = "건물 단건 조회 [3.2]", description = "buildingId로 건물 하나를 조회하는 API")
    @Parameter(name = "buildingId", description = "조회하고자 하는 건물의 id", example = "4454")
    @GetMapping("/{buildingId}")
    public ResponseEntity<BuildingResponseDto.BuildingResponse> getBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId, @AuthUser Member member) {
        Boolean isFavorite = Boolean.FALSE;
        Building building = this.buildingService.getBuildingByBuildingId(buildingId).orElseThrow(() -> new BuildingException(ErrorCode.BUILDING_NOT_FOUND));
        building.getBuildingToReviewCategoryList().stream().forEach(Hibernate::initialize);
        if (member != null) isFavorite = favoriteService.existsByBuildingAndMember(building, member);
        return ResponseEntity.ok(BuildingSerializer.toBuildingResponse(building, isFavorite));
    }

    /* 8.1
    when:
    request: 검색 코드(Enum), searchParams(주소,단일 Or 복수, 집 주소 등등)
    service:
        - 검색 코드를 바탕으로 동적으로 각자 다른 클래스를 호출하도록 동적으로 처리
    return: 건물 정보
     */
    @Operation(summary = "건물 검색 조회 [8.1]", description = "건물명 또는 주소로 검색하여 조회하는 API")
    @Parameters({
            @Parameter(name = "params", description = "검색 내용", example = "영통"),
            @Parameter(name = "cursorIds", description = "커서 id", example = "4314"),
            @Parameter(name = "size", description = "응답 건물 개수", example = "4"),
            @Parameter(name = "sort", description = "정렬 기준", example = "id,DESC"),
            @Parameter(name = "pageable", hidden = true)
    })
    @GetMapping("/search")
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> searchBuilding(@RequestParam("params") String params, @RequestParam(required = false) List<Double> cursorIds, @PageableDefault(size = 10, sort = "id", page = 0, direction = Sort.Direction.DESC) Pageable pageable) {
        if (cursorIds == null) cursorIds = new ArrayList<>();
        List<Building> buildingList = this.buildingService.getBuildingsBySearch(params, cursorIds, pageable);
        List<BuildingResponseDto.BuildingListResponse> buildingListResponse = buildingList.stream().map(BuildingSerializer::toBuildingListResponse).collect(Collectors.toList());
        return ResponseEntity.ok(QueryDslUtil.toSlice(buildingListResponse, pageable));
    }

    /*
    building set generator for test
     */
    @Operation(summary = "건물 생성", description = "테스트를 위한 건물 생성 API")
    @PostMapping("")
    public ResponseEntity<BuildingResponseDto.BuildingMetaData> createBuilding(@RequestBody BuildingRequestDto.BuildingCreateRequest request) {
        Building building = this.buildingService.createBuilding(AddressDto.toAddress(request.getAddressDto()), request.getBuildingOptionalDto());
        return ResponseEntity.ok(BuildingSerializer.toBuildingMetaData(building));
    }


    @Operation(summary = "건물에 대한 리뷰 이미지 조회 [3.2]", description = "리뷰 이미지 조회 by buildingId API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = BuildingResponseDto.ReviewImageListDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "buildingId", description = "이미지를 조회하고자하는 건물의 id", example = "1004")
    })
    @GetMapping("/{buildingId}/images")
    public ResponseEntity<BuildingResponseDto.ReviewImageListDto> getBuildingImageList(@PathVariable("buildingId") @ExistBuilding Long buildingId) {
        List<ReviewImage> reviewImageList = reviewImageService.findByBuilding(buildingId);
        return ResponseEntity.ok(BuildingSerializer.toReviewImageListDto(reviewImageList));
    }
}
