package com.project.Project.controller.interaction.controller;

import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.controller.interaction.dto.FavoriteResponseDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.enums.MemberRole;
import com.project.Project.service.FavoriteService;
import com.project.Project.validator.ExistBuilding;
import com.project.Project.validator.interaction.ExistFavorite;
import com.project.Project.validator.interaction.FavoriteExistValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;


@RestController
@RequiredArgsConstructor
public class FavoriteRestController {
    // todo : 사용자 정보 파라미터 모든 메소드에 필요

    private final FavoriteService favoriteService;

    private Member getTestMember() {
         return  Member.builder() // temp user
                 .reviewList(new ArrayList<>())
                 .favoriteBuildingList(new ArrayList<>())
                 .likeReviewList(new ArrayList<>())
                 .name("하품하는 망아지")
                 .email("swa07016@khu.ac.kr")
                 .memberRole(MemberRole.USER)
                 .refreshToken("mockingMember")
                 .profileImageUrl("https://lh3.googleusercontent.com/ogw/AOh-ky20QeRrWFPI8l-q3LizWDKqBpsWTIWTcQa_4fh5=s64-c-mo")
                 .build();
    }

    @GetMapping("/member/favorite")
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> getFavoriteBuildingList() {
        return null;
    }

    @PostMapping("/member/favorite")
    public ResponseEntity<FavoriteResponseDto.FavoriteAddResponse> addFavoriteBuilding(@ExistBuilding Long buildingId) {
        Long savedFavoriteId = favoriteService.addFavoriteBuilding(buildingId, getTestMember());
        return ResponseEntity.ok(FavoriteResponseDto.FavoriteAddResponse.builder()
                .favoriteId(savedFavoriteId)
                .createdAt(LocalDateTime.now())
                .build()
        );
    }

    @DeleteMapping("/member/favorite")
    public ResponseEntity<FavoriteResponseDto.FavoriteDeleteResponse> deleteFavoriteBuilding(@ExistBuilding Long buildingId) {
        Member member = getTestMember();
        @ExistFavorite
        FavoriteExistValidator.FavoriteExistVO favoriteExistVO =
                FavoriteExistValidator.FavoriteExistVO.builder().member(member).buildingId(buildingId).build();

        Long deletedFavoriteId = favoriteService.deleteFavoriteBuilding(buildingId, member);
        return ResponseEntity.ok(FavoriteResponseDto.FavoriteDeleteResponse.builder()
                .favoriteId(deletedFavoriteId)
                .deletedAt(LocalDateTime.now())
                .build());
    }
}
