package com.project.Project.common.serializer.checklist;

import com.project.Project.controller.checklist.dto.ChecklistResponseDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.controller.room.dto.RoomDto;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.embedded.Address;

import java.util.stream.Collectors;

public class ChecklistSerializer {

    public static ChecklistResponseDto.ChecklistElement toChecklistElement(CheckList checkList) {
        return ChecklistResponseDto.ChecklistElement.builder()
                .address(Address.toAddressDto(checkList.getBuilding().getAddress()))
                .room(new RoomDto.RoomBaseDto(checkList.getLineNum(), checkList.getRoomNum()))
                .assessment(toChecklistAssessment(checkList))
                .memo(checkList.getMemo())
                .checklistImageListDto(ChecklistResponseDto.ChecklistImageListDto.builder()
                        .reviewImageList(checkList.getCheckListImageList().stream().map(ChecklistSerializer::toChecklistImageDto).collect(Collectors.toList()))
                        .reviewImageCount(checkList.getCheckListImageList().size()).build())
                .createdAt(checkList.getCreatedAt())
                .updatedAt(checkList.getUpdatedAt())
                .build();
    }

    public static ChecklistResponseDto.ChecklistImageDto toChecklistImageDto(CheckListImage checklistImage) {
        return ChecklistResponseDto.ChecklistImageDto.builder()
                .uuid(checklistImage.getUuid().getUuid())
                .url(checklistImage.getUrl())
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
                .score(checkList.getScore())
                .build();
    }

    public static ChecklistResponseDto.ChecklistCreateDto toChecklistCreateDto(CheckList savedCheckList) {
        return null;
    }
}
