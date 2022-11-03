package com.project.Project.controller.interaction.controller;

import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.controller.interaction.dto.FavoriteResponseDto;
import com.project.Project.validator.ExistBuilding;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class FavoriteRestController {
    // todo : 사용자 정보 파라미터 모든 메소드에 필요

    @GetMapping("/member/favorite")
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> getFavoriteBuildingList() {
        return null;
    }

    @PostMapping("/member/favorite")
    public ResponseEntity<FavoriteResponseDto.FavoriteAddResponse> addFavoriteBuilding(@ExistBuilding Long buildingId) {
        return null;
    }

    @DeleteMapping("/member/favorite")
    public ResponseEntity<FavoriteResponseDto.FavoriteDeleteResponse> deleteFavoriteBuilding(@ExistBuilding Long buildingId) {
        return null;
    }
}
