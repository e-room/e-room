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

@Validated
@RestController
@RequiredArgsConstructor
public class FavoriteRestController {

    private final FavoriteService favoriteService;
    private final FavoriteExistValidator favoriteExistValidator;

    @GetMapping("/member/favorite")
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> getFavoriteBuildingList(@RequestParam(required = false) List<Double> cursorIds, @PageableDefault(size = 10, sort = "id", page = 0, direction = Sort.Direction.DESC) Pageable pageable, @AuthUser Member member) {
        if (cursorIds == null) cursorIds = new ArrayList<>();
        List<Building> buildingList = favoriteService.getBuildingListByMember(member, cursorIds, pageable);
        List<BuildingResponseDto.BuildingListResponse> buildingListResponse = buildingList.stream().map((building) -> BuildingSerializer.toBuildingListResponse(building)).collect(Collectors.toList());
        return ResponseEntity.ok(QueryDslUtil.toSlice(buildingListResponse, pageable));
    }

    @PostMapping("/member/favorite/{buildingId}")
    public ResponseEntity<FavoriteResponseDto.FavoriteAddResponse> addFavoriteBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId, @AuthUser Member member) {
        if(favoriteExistValidator.exists(member, buildingId))
            throw new FavoriteException(ErrorCode.FAVORITE_ALREADY_EXISTS);

        Long savedFavoriteId = favoriteService.addFavoriteBuilding(buildingId, member);
        return ResponseEntity.ok(FavoriteSerializer.toFavoriteAddResponse(savedFavoriteId));
    }

    @DeleteMapping("/member/favorite/{buildingId}")
    public ResponseEntity<FavoriteResponseDto.FavoriteDeleteResponse> deleteFavoriteBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId, @AuthUser Member member) {
        if (!favoriteExistValidator.exists(member, buildingId))
            throw new FavoriteException(ErrorCode.FAVORITE_NOT_FOUND);

        Long deletedFavoriteId = favoriteService.deleteFavoriteBuilding(buildingId, member);
        return ResponseEntity.ok(FavoriteSerializer.toFavoriteDeleteResponse(deletedFavoriteId));
    }
}
