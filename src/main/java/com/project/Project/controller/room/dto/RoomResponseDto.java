package com.project.Project.controller.room.dto;

import com.project.Project.domain.enums.LightDirection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RoomResponseDto {

    @Builder
    @Getter
    @Setter
    public static class RoomResponse{
        private Integer roomId;
        private Integer roomNumber;
        private BigDecimal netLeasableArea;
        private LightDirection lightDirection;
        private Integer rentFee;
        private Integer managementFee;
    }

    @Builder
    @Getter
    @Setter
    public static class RoomListResponse{
        private Integer roomId;
        private Integer roomNumber;
    }

    @Builder
    @Getter
    @Setter
    public static class RoomCreateResponse{
        private Integer roomId;
        private Integer buildingId;
        private LocalDateTime createdAt;
        private
    }
}
