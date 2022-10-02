package com.project.Project.controller.building.dto;


import com.project.Project.controller.room.dto.RoomResponseDto;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BuildingResponseDto {

    /*
    여러 Building들의 List를 내려주기 위함.
     */
    @Builder
    @Getter
    public static class BuildingListResponse{
        private Long buildingId;
        private String name;
        private String address;
        private Long reviewCnt;
        private Double scoreAvg;
        private ReviewCategoryEnum bestCategory;
        private boolean isDirectDeal;
    }

    /*
    지도 마킹용
     */
    @Builder
    @Getter
    public static class BuildingCountResponse{
        private CoordinateDto coordinateDto;
        private Long buildingId;
    }


    /*
    단일 건물에 대한 자세한 정보를 보여주기 위함
     */
    @Builder
    @Getter
    public static class BuildingResponse{
        private Long buildingId;
        private String name;
        private String address;
        private CoordinateDto coordinate;
        private Boolean isDirectDeal;
        private List<RoomResponseDto.RoomListResponse> rooms;
        private Map<ReviewCategoryEnum, Double> buildingSummaries;
    }
}

