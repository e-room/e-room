package com.project.Project.serializer.building;

import com.project.Project.controller.building.dto.BuildingResponseDto;
import com.project.Project.controller.room.dto.RoomResponseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.building.BuildingToReviewCategory;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.serializer.room.RoomSerializer;

import java.util.*;
import java.util.stream.Collectors;

public class BuildingSerializer {

    public static BuildingResponseDto.BuildingResponse toBuildingResponse(Building building) {

        List<BuildingToReviewCategory> buildingToReviewCategoryList = building.getBuildingToReviewCategoryList();
        Map<ReviewCategoryEnum, Double> buildingSummary = new HashMap<>();
        //with stream, if the list is empty, it does nothing
        buildingToReviewCategoryList.stream().forEach(
                buildingToReviewCategory -> {
                    // when list contains null elem, NPE occurs, we'd like to protect that case
                    ReviewCategoryEnum category = BuildingToReviewCategory.bestCategoryOrNull.apply(buildingToReviewCategory);
                    Double avgScore = buildingToReviewCategory.getAvgScore();
                    if (category != null || avgScore != null) {
                        buildingSummary.put(category, avgScore);
                    }
                }
        );
        //with stream, if the list is empty, stream map method return empty list
        return BuildingResponseDto.BuildingResponse.builder()
                .buildingId(building.getId())
                .name(building.getBuildingName())
                .address(Address.toAddressDto(building.getAddress()))
                .coordinate(Coordinate.toCoordinateDto(building.getCoordinate()))
                .isDirectDeal(false)
                .rooms(building.getRoomList().stream().map(RoomSerializer::toRoomListResponse).collect(Collectors.toList()))
                .buildingSummaries(buildingSummary)
                .build();
    }

    public static BuildingResponseDto.BuildingListResponse toBuildingListResponse(Building building) {
        //protect NPE when building is null
        Optional.ofNullable(building).orElseThrow(() -> new BuildingException(ErrorCode.BUILDING_NPE));

        BuildingToReviewCategory maxScoreCategory = building.getBuildingToReviewCategoryList().stream().max(Comparator.comparing(BuildingToReviewCategory::getAvgScore)).orElse(null);
        return BuildingResponseDto.BuildingListResponse.builder()
                .buildingId(building.getId())
                .name(building.getBuildingName())
                .address(Address.toAddressDto(building.getAddress()))
                .isDirectDeal(false)
                .reviewCnt(Building.reviewCntOrZero.apply(building))
                .avgScore(Building.avgScoreOrNull.apply(building))
                .bestCategory(BuildingToReviewCategory.bestCategoryOrNull.apply(maxScoreCategory))
                .build();
    }

    public static BuildingResponseDto.BuildingMarkerResponse toBuildingMarkerResponse(OnlyBuildingIdAndCoord building) {
        //protect NPE when building is null
        Optional.ofNullable(building).orElseThrow(() -> new BuildingException(ErrorCode.BUILDING_NPE));

        return BuildingResponseDto.BuildingMarkerResponse.builder()
                .buildingId(building.getId())
                .coordinateDto(Coordinate.toCoordinateDto(building.getCoordinate()))
                .build();
    }

    public static BuildingResponseDto.BuildingCountResponse toBuildingCountResponse(List<OnlyBuildingIdAndCoord> buildingList) {
        return BuildingResponseDto.BuildingCountResponse.builder()
                .buildingCount(buildingList.size())
                .buildingList(buildingList.stream().map(BuildingSerializer::toBuildingMarkerResponse).collect(Collectors.toList()))
                .build();
    }

    public static BuildingResponseDto.BuildingMetaData toBuildingMetaData(Building building) {
        //protect NPE when building is null
        Optional.ofNullable(building).orElseThrow(() -> new BuildingException(ErrorCode.BUILDING_NPE));

        return BuildingResponseDto.BuildingMetaData.builder()
                .buildingId(building.getId())
                .name(building.getBuildingName())
                .address(Address.toAddressDto(building.getAddress()))
                .isDirectDeal(false)
                .coordinateDto(Coordinate.toCoordinateDto(building.getCoordinate()))
                .build();
    }

    public static BuildingResponseDto.ReviewImageDto toReviewImageDto(ReviewImage reviewImage) {
        return BuildingResponseDto.ReviewImageDto.builder()
                .url(reviewImage.getUrl())
                .uuid(reviewImage.getUuid().getUuid())
                .build();
    }

    public static BuildingResponseDto.ReviewImageListDto toReviewImageListDto(List<ReviewImage> reviewImageList) {
        List<BuildingResponseDto.ReviewImageDto> reviewImageDtoList =
                reviewImageList.stream()
                        .map((reviewImage -> toReviewImageDto(reviewImage)))
                        .collect(Collectors.toList());
        Integer reviewImageCount = reviewImageDtoList.size();

        return BuildingResponseDto.ReviewImageListDto.builder()
                .reviewImageList(reviewImageDtoList)
                .reviewImageCount(reviewImageCount)
                .build();
    }

}
