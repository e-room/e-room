package com.project.Project.controller.room.dto;

import com.project.Project.domain.enums.LightDirection;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RoomResponseDto {

    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class RoomResponse{
        private Long roomId;
        private Integer roomNumber;
        private BigDecimal netLeasableArea;
        private LightDirection lightDirection;
        private Integer rentFee;
        private Integer managementFee;
    }

    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class RoomListResponse{
        private Long roomId;
        private Integer roomNumber;
    }

    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class RoomCreateResponse{
        private Long roomId;
        private Long buildingId;
        private LocalDateTime createdAt;
        private Integer roomNumber;
    }
}
