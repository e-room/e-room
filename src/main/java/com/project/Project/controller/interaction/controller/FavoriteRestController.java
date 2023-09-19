package com.project.Project.controller.interaction.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.controller.interaction.dto.FavoriteResponseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.member.Member;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.interaction.FavoriteException;
import com.project.Project.serializer.building.BuildingSerializer;
import com.project.Project.serializer.interaction.FavoriteSerializer;
import com.project.Project.service.FavoriteService;
import com.project.Project.util.component.QueryDslUtil;
import com.project.Project.validator.ExistBuilding;
import com.project.Project.validator.interaction.FavoriteExistValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

@Tag(name = "04-Favorite 🙌",description = "찜한 자취방 추가, 삭제, 조회")
@Validated
@RestController
@RequiredArgsConstructor
public class FavoriteRestController {

    private final FavoriteService favoriteService;
    private final FavoriteExistValidator favoriteExistValidator;

    @Operation(summary = "사용자의 찜 목록 조회 [5]", description = "찜 목록 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "cursorIds", description = "커서 id", example = "2.4,8714"),
            @Parameter(name = "size", description = "응답 건물 개수", example = "4"),
            @Parameter(name = "sort", description = "정렬 기준", example = "id,DESC"),
            @Parameter(name = "pageable", hidden = true),
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/member/favorite")
    public ResponseEntity<Slice<BuildingResponseDto.BuildingElement>> getFavoriteBuildingList(@RequestParam(required = false) List<Double> cursorIds, @PageableDefault(size = 10, sort = "id", page = 0, direction = Sort.Direction.DESC) Pageable pageable, @AuthUser Member member) {
        if (cursorIds == null) cursorIds = new ArrayList<>();
        List<Building> buildingList = favoriteService.getBuildingListByMember(member, cursorIds, pageable);
        List<BuildingResponseDto.BuildingElement> buildingElement = buildingList.stream().map((building) -> BuildingSerializer.toBuildingListResponse(building)).collect(Collectors.toList());
        return ResponseEntity.ok(QueryDslUtil.toSlice(buildingElement, pageable));
    }

    @Operation(summary = "건물 찜하기 [3.2]", description = "건물 찜하기 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = FavoriteResponseDto.FavoriteAddResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "buildingId", description = "찜하고자 하는 건물의 id"),
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("/member/favorite/{buildingId}")
    public ResponseEntity<FavoriteResponseDto.FavoriteAddResponse> addFavoriteBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId, @AuthUser Member member) {
        if (favoriteExistValidator.exists(member, buildingId))
            throw new FavoriteException(ErrorCode.FAVORITE_ALREADY_EXISTS);

        Long savedFavoriteId = favoriteService.addFavoriteBuilding(buildingId, member);
        return ResponseEntity.ok(FavoriteSerializer.toFavoriteAddResponse(savedFavoriteId));
    }

    @Operation(summary = "건물 찜 삭제 [3.2]", description = "건물 찜 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = FavoriteResponseDto.FavoriteDeleteResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "buildingId", description = "찜한 건물중 삭제하고자 하는 건물의 id"),
            @Parameter(name = "member", hidden = true)
    })
    @DeleteMapping("/member/favorite/{buildingId}")
    public ResponseEntity<FavoriteResponseDto.FavoriteDeleteResponse> deleteFavoriteBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId, @AuthUser Member member) {
        if (!favoriteExistValidator.exists(member, buildingId))
            throw new FavoriteException(ErrorCode.FAVORITE_NOT_FOUND);

        Long deletedFavoriteId = favoriteService.deleteFavoriteBuilding(buildingId, member);
        return ResponseEntity.ok(FavoriteSerializer.toFavoriteDeleteResponse(deletedFavoriteId));
    }
}
