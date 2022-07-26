package com.project.Project.controller.building.dto;


import com.project.Project.controller.room.dto.RoomResponseDto;
import com.project.Project.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BuildingResponseDto {

    /*
    여러 Building들의 List를 내려주기 위함.
     */
    @Builder
    @Getter
    @Setter
    public static class BuildingListResponse{
        private Integer buildingId;
        private String name;
        private String address;
        private Integer reviewCnt;
        private Float scoreAvg;
        private boolean isDirectDeal;
    }

    /*
    지도 마킹용
     */
    @Builder
    @Getter
    @Setter
    public static class BuildingCountResponse{
        private Coordinate coordinate;
        private Integer buildingId;
    }


    /*
    단일 건물에 대한 자세한 정보를 보여주기 위함
     */
    @Builder
    @Getter
    @Setter
    public static class BuildingResponse{
        private Integer buildingId;
        private String name;
        private String address;
        private Coordinate coordinate;
        private List<RoomResponseDto.RoomListResponse> rooms;
        private List<ReviewDto> reviews;
        /* todo
        private List<BuildingSummary> buildingSummaries;
         */
        private Integer initalReviewCount;
    }
}
