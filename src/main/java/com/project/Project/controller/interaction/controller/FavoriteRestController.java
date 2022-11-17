package com.project.Project.controller.interaction.controller;

import com.project.Project.Util.QueryDslUtil;
import com.project.Project.auth.AuthUser;
import com.project.Project.util.component.QueryDslUtil;
import com.project.Project.controller.CursorDto;
import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.controller.interaction.dto.FavoriteResponseDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.enums.MemberRole;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.exception.interaction.FavoriteException;
import com.project.Project.serializer.building.BuildingSerializer;
import com.project.Project.service.FavoriteService;
import com.project.Project.validator.ExistBuilding;

import com.project.Project.validator.interaction.FavoriteExistValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
public class FavoriteRestController {
    // todo : 사용자 정보 파라미터 모든 메소드에 필요

    private final FavoriteService favoriteService;
    private final FavoriteExistValidator favoriteExistValidator;

    @GetMapping("/member/favorite")
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> getFavoriteBuildingList(@RequestBody CursorDto cursorDto, @AuthUser Member member) {
        Pageable pageable = PageRequest.of(0, cursorDto.getSize());
        List<Building> buildingList = favoriteService.getBuildingListByMember(member, cursorDto.getCursorId(), PageRequest.of(0, cursorDto.getSize()));
        List<BuildingResponseDto.BuildingListResponse> buildingListResponse = buildingList.stream().map((building) -> BuildingSerializer.toBuildingListResponse(building)).collect(Collectors.toList());
        return ResponseEntity.ok(QueryDslUtil.toSlice(buildingListResponse, pageable));
    }

    @PostMapping("/member/favorite/{buildingId}")
    public ResponseEntity<FavoriteResponseDto.FavoriteAddResponse> addFavoriteBuilding(@ExistBuilding Long buildingId, @AuthUser Member member) {
        Long savedFavoriteId = favoriteService.addFavoriteBuilding(buildingId, member);
        return ResponseEntity.ok(FavoriteResponseDto.FavoriteAddResponse.builder()
                .favoriteId(savedFavoriteId)
                .createdAt(LocalDateTime.now())
                .build()
        );
    }

    @DeleteMapping("/member/favorite/{buildingId}")
    public ResponseEntity<FavoriteResponseDto.FavoriteDeleteResponse> deleteFavoriteBuilding(@ExistBuilding Long buildingId, @AuthUser Member member) {
        if(!favoriteExistValidator.isValid(member, buildingId))
            throw new FavoriteException(ErrorCode.FAVORITE_NOT_FOUND);

        Long deletedFavoriteId = favoriteService.deleteFavoriteBuilding(buildingId, member);
        return ResponseEntity.ok(FavoriteResponseDto.FavoriteDeleteResponse.builder()
                .favoriteId(deletedFavoriteId)
                .deletedAt(LocalDateTime.now())
                .build());
    }
}
