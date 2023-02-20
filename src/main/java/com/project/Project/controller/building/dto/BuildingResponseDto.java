package com.project.Project.controller.building.dto;

import com.project.Project.domain.enums.DirectDealType;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

public class BuildingResponseDto {

    /*
    여러 Building들의 List를 내려주기 위함.
     */
    @Builder
    @Getter
    public static class BuildingListResponse {
        private Long buildingId;
        private String name;
        private AddressDto address;
        private Long reviewCnt;
        private Double avgScore;
        private ReviewCategoryEnum bestCategory;
        private DirectDealType directDealType;
    }

    @Builder
    @Getter
    public static class BuildingCountResponse {
        private List<BuildingMarkerResponse> buildingList;
        private Integer buildingCount;
    }

    /*
    지도 마킹용
     */
    @Builder
    @Getter
    public static class BuildingMarkerResponse {
        private CoordinateDto coordinateDto;
        private Long buildingId;
    }


    /*
    단일 건물에 대한 자세한 정보를 보여주기 위함
     */
    @Builder
    @Getter
    public static class BuildingResponse {
        private Long buildingId;
        private String name;
        private AddressDto address;
        private CoordinateDto coordinate;
        private DirectDealType directDealType;
        private Boolean isFavorite;
        private Map<ReviewCategoryEnum, Double> buildingSummaries;
    }

    @Builder
    @Getter
    public static class BuildingMetaData {
        private Long buildingId;
        private String name;
        private AddressDto address;
        private CoordinateDto coordinateDto;
        private DirectDealType directDealType;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewImageDto {
        private String uuid;
        private String url;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewImageListDto {
        private List<BuildingResponseDto.ReviewImageDto> reviewImageList;
        private Integer reviewImageCount;
    }
}

