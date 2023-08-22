package com.project.Project.common.serializer.checklist;

import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.checklist.dto.ChecklistRequestDto;
import com.project.Project.controller.checklist.dto.ChecklistResponseDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.controller.room.dto.RoomDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.member.Member;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.checklist.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChecklistSerializer {

    private final ChecklistRepository checklistRepository;
    private final BuildingRepository buildingRepository;

    private static ChecklistRepository staticChecklistRepository;
    private static BuildingRepository staticBuildingRepository;

    @PostConstruct
    public void init() {
        staticChecklistRepository = this.checklistRepository;
        staticBuildingRepository = this.buildingRepository;
    }

    public static ChecklistResponseDto.ChecklistElement toChecklistElement(CheckList checkList) {
        return ChecklistResponseDto.ChecklistElement.builder()
                .address(Address.toAddressDto(checkList.getBuilding().getAddress()))
                .room(new RoomDto.RoomBaseDto(checkList.getLineNum(), checkList.getRoomNum()))
                .assessment(toChecklistAssessment(checkList))
                .memo(checkList.getMemo())
                .checklistImageListDto(ChecklistResponseDto.ChecklistImageListDto.builder()
                        .checklistImageList(checkList.getCheckListImageList().stream().map(ChecklistSerializer::toChecklistImageDto).collect(Collectors.toList()))
                        .checklistImageCount(checkList.getCheckListImageList().size()).build())
                .createdAt(checkList.getCreatedAt())
                .updatedAt(checkList.getUpdatedAt())
                .build();
    }

    public static ChecklistResponseDto.ChecklistImageListDto toChecklistImageListDto(List<CheckListImage> checkListImages) {
        return ChecklistResponseDto.ChecklistImageListDto.builder()
                .checklistImageList(checkListImages.stream().map(ChecklistSerializer::toChecklistImageDto).collect(Collectors.toList()))
                .checklistImageCount(checkListImages.size()).build();
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
        return ChecklistResponseDto.ChecklistCreateDto.builder()
                .checklistId(savedCheckList.getId())
                .createdAt(savedCheckList.getCreatedAt())
                .build();
    }

    public static CheckList toChecklist(ChecklistRequestDto.ChecklistCreateDto request, Member member) {

        Building building = request.getHasBuildingAddress() ? staticBuildingRepository.findBuildingById(request.getBuildingId()) : null;
        String nickname = request.getHasBuildingAddress() ? null : request.getNickname();

        return staticChecklistRepository.save(CheckList.builder()
                .author(member)
                .building(building)
                .nickname(nickname)
                .checkListResponses(new ArrayList<>())
                .checkListImageList(new ArrayList<>())
                .memo("")
                .score(0.0)
                .lineNum(request.getLineNum())
                .roomNum(request.getRoomNum())
                .monthlyRent(request.getMonthlyRent())
                .managementFee(request.getManagementFee())
                .deposit(request.getDeposit())
                .netLeasableArea(request.getNetLeasableArea())
                .build());
    }

    public static ChecklistResponseDto.ChecklistImageDeleteDto toChecklistImageDeleteDto(Long checklistImageId, List<CheckListImage> remainedImages) {
        return ChecklistResponseDto.ChecklistImageDeleteDto.builder()
                .deletedImageId(checklistImageId)
                .remainedImages(toChecklistImageListDto(remainedImages))
                .build();
    }


    public static ChecklistResponseDto.ChecklistDeleteDto toChecklistDeletedDto(Long deletedChecklistId) {
        return ChecklistResponseDto.ChecklistDeleteDto.builder()
                .checklistId(deletedChecklistId)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static ChecklistResponseDto.ChecklistQuestionUpdateDto toChecklistQuestionUpdateDto(CheckListQuestion updatedChecklistQuestion) {
        return ChecklistResponseDto.ChecklistQuestionUpdateDto.builder()
                .checklistId(updatedChecklistQuestion.getCheckList().getId())
                .questionId(updatedChecklistQuestion.getQuestion().getId())
                .updatedAt(updatedChecklistQuestion.getUpdatedAt())
                .build();
    }

    public static List<ChecklistResponseDto.CheckListDto> toMemberCheckListDto(List<CheckList> checkLists) {
        List<ChecklistResponseDto.CheckListDto> checkListDtoList = new ArrayList<>();

        for(CheckList checkList : checkLists) {

            AddressDto addressDto = null;
            if(checkList.getBuilding() != null) {
                addressDto = AddressDto.builder()
                        .siDo(checkList.getBuilding().getAddress().getSiDo())
                        .siGunGu(checkList.getBuilding().getAddress().getSiGunGu())
                        .eupMyeon(checkList.getBuilding().getAddress().getEupMyeon())
                        .roadName(checkList.getBuilding().getAddress().getRoadName())
                        .buildingNumber(checkList.getBuilding().getAddress().getBuildingNumber())
                        .build();
            }

            ChecklistResponseDto.CheckListDto checkListDto = ChecklistResponseDto.CheckListDto.builder()
                    .checkListId(checkList.getId())
                    .authorId(checkList.getAuthor().getId())
                    .buildingId(checkList.getBuilding() == null ? null : checkList.getBuilding().getId())
                    .address(addressDto)
                    .nickname(checkList.getNickname())
                    .score(checkList.getScore())
                    .netLeasableArea(checkList.getNetLeasableArea())
                    .lineNum(checkList.getLineNum())
                    .roomNum(checkList.getRoomNum())
                    .deposit(checkList.getDeposit())
                    .monthlyRent(checkList.getMonthlyRent())
                    .managementFee(checkList.getManagementFee())
                    .createdAt(checkList.getCreatedAt())
                    .updatedAt(checkList.getUpdatedAt())
                    .build();

            checkListDtoList.add(checkListDto);
        }

        return checkListDtoList;
    }

}
