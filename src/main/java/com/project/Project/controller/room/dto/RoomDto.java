package com.project.Project.controller.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RoomDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class RoomBaseDto {
        private String lineNum;
        private String roomNum;
    }
}
