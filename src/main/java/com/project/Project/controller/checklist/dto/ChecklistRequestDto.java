package com.project.Project.controller.checklist.dto;

import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.domain.enums.Expression;
import lombok.Getter;

import javax.persistence.Column;

public class ChecklistRequestDto {

    public static class ChecklistDto {
        
    }

    @Getter
    public static class ChecklistCreateDto {
        private Boolean hasBuildingAddress;
        private AddressDto address;
        private String nickname;
        private String lineNum;
        private String roomNum;
        private Double monthlyRent;
        private Double managementFee;
        private Double deposit;
        private Double netLeasableArea;
    }

    @Getter
    public static class ChecklistQuestionUpdateDto {
        private Expression expression;
        private String memo;
    }

    @Getter
    public static class ChecklistUpdateDto {
        private Boolean hasBuildingAddress;
        private AddressDto address;
        private String nickname;
        private String lineNum;
        private String roomNum;
        private Double monthlyRent;
        private Double managementFee;
        private Double deposit;
        private Double netLeasableArea;
        private String memo;
        private Double score;
    }
}
