package com.project.Project.controller.checklist.dto;

import lombok.Getter;

import javax.persistence.Column;

public class ChecklistRequestDto {

    public static class ChecklistDto {
        
    }

    @Getter
    public static class ChecklistCreateDto {
        private Boolean hasBuildingAddress;
        private Long buildingId;
        private String nickname;
        private String lineNum;
        private String roomNum;
        private Double monthlyRent;
        private Double managementFee;
        private Double deposit;
        private Double netLeasableArea;
    }
}
