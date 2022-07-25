package com.project.Project.controller.building.buildingDto;


import com.project.Project.domain.Review;
import com.project.Project.domain.Room;
import com.project.Project.domain.embedded.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

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
        private Map<Coordinate, Integer> buildingMarker;
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
        private List<Room> rooms;
        private List<Review> reviews;
    }
}
