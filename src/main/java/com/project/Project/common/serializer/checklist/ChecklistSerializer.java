package com.project.Project.common.serializer.checklist;

import com.project.Project.controller.checklist.dto.ChecklistResponseDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.controller.room.dto.RoomDto;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.embedded.Address;

public class ChecklistSerializer {

    public static ChecklistResponseDto.ChecklistElement toChecklistElement(CheckList checkList) {
        return ChecklistResponseDto.ChecklistElement.builder()
                .address(Address.toAddressDto(checkList.getBuilding().getAddress()))
                .room(new RoomDto.RoomBaseDto(checkList.getLineNum(), checkList.getRoomNum()))
                .assessment(toChecklistAssessment(checkList))
                .createdAt(checkList.getCreatedAt())
                .updatedAt(checkList.getUpdatedAt())
                .build();
    }

    public static ChecklistResponseDto.ChecklistAssessment toChecklistAssessment(CheckList checkList) {
        return ChecklistResponseDto.ChecklistAssessment.builder()
                .basicAssessment(ReviewResponseDto.BasicAssessmentDto
                        .builder()
                        .netLeasableArea(checkList.getNetLeasableArea())
                        .deposit(checkList.getDeposit())
                        .monthlyRent(checkList.getMonthlyRent())
                        .managementFee(checkList.getManagementFee()).build())
                .checkListResponses(checkList.getCheckListResponses())
                .memo(checkList.getMemo())
                .score(checkList.getScore())
                .checkListImages(checkList.getCheckListImageList())
                .build();
    }
}
