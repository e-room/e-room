package com.project.Project.controller.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class RoomResponseDto {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class BaseRoomDto {
        private Long roomId;
        private Integer roomNumber;
        private Integer lineNumber;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class RoomListDto {
        private Long roomId;
        private Integer roomNumber;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class RoomCreateDto {
        private Long roomId;
        private Long buildingId;
        private LocalDateTime createdAt;
        private Integer roomNumber;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class RoomImageDto {
        private String uuid;
        private String url;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class RoomImageListDto {
        private List<RoomImageDto> roomImageList;
        private Integer roomImageCount;
    }
}
