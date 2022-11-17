package com.project.Project.controller.interaction.controller;

import com.project.Project.Util.QueryDslUtil;
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
    public ResponseEntity<Slice<BuildingResponseDto.BuildingListResponse>> getFavoriteBuildingList(@RequestBody CursorDto cursorDto) {
        Pageable pageable = PageRequest.of(0, cursorDto.getSize());
        List<Building> buildingList = favoriteService.getBuildingListByMember(getTestMember(), cursorDto.getCursorId(), PageRequest.of(0, cursorDto.getSize()));
        List<BuildingResponseDto.BuildingListResponse> buildingListResponse = buildingList.stream().map((building) -> BuildingSerializer.toBuildingListResponse(building)).collect(Collectors.toList());
        return ResponseEntity.ok(QueryDslUtil.toSlice(buildingListResponse, pageable));
    }

    @PostMapping("/member/favorite/{buildingId}")
    public ResponseEntity<FavoriteResponseDto.FavoriteAddResponse> addFavoriteBuilding(@ExistBuilding Long buildingId) {
        Long savedFavoriteId = favoriteService.addFavoriteBuilding(buildingId, getTestMember());
        return ResponseEntity.ok(FavoriteResponseDto.FavoriteAddResponse.builder()
                .favoriteId(savedFavoriteId)
                .createdAt(LocalDateTime.now())
                .build()
        );
    }

    @DeleteMapping("/member/favorite/{buildingId}")
    public ResponseEntity<FavoriteResponseDto.FavoriteDeleteResponse> deleteFavoriteBuilding(@ExistBuilding Long buildingId) {
        Member member = getTestMember();
        if(!favoriteExistValidator.isValid(member, buildingId))
            throw new FavoriteException(ErrorCode.FAVORITE_NOT_FOUND);

        Long deletedFavoriteId = favoriteService.deleteFavoriteBuilding(buildingId, member);
        return ResponseEntity.ok(FavoriteResponseDto.FavoriteDeleteResponse.builder()
                .favoriteId(deletedFavoriteId)
                .deletedAt(LocalDateTime.now())
                .build());
    }
}
