package com.project.Project.controller.checklist.dto;

import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.controller.room.dto.RoomDto;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.checklist.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public class ChecklistResponseDto {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ChecklistAssessment {
        private ReviewResponseDto.BasicAssessmentDto basicAssessment;
        private List<CheckListQuestion> checkListResponses;
        private Double score;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @SuperBuilder
    public static class ChecklistElement extends ReviewResponseDto.BasicAssessmentDto {
        @Nullable
        private AddressDto address;
        private RoomDto.RoomBaseDto room;
        private ChecklistAssessment assessment;
        private String memo;
        private ChecklistImageListDto checklistImageListDto;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ChecklistImageDto {
        private String uuid;
        private String url;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ChecklistImageListDto {
        private List<ChecklistImageDto> checklistImageList;
        private Integer checklistImageCount;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ChecklistImageDeleteDto {
        private ChecklistImageListDto remainedImages;
        private Long deletedImageId;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ChecklistCreateDto {
        private Long checklistId;
        private LocalDateTime createdAt;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ChecklistDeleteDto {
        private Long checklistId;
        private LocalDateTime deletedAt;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ChecklistQuestionUpdateDto {
        private Long checklistId;
        private Long questionId;
        private LocalDateTime updatedAt;

    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder

    public static class CheckListDto {
        private Long checkListId;
        private Long authorId;
        private Long buildingId;
        private AddressDto address;
        private String nickname;
        private Double score;
        private Double netLeasableArea;
        private String lineNum;
        private String roomNum;
        private Double deposit;
        private Double monthlyRent;
        private Double managementFee;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }

    public static class QuestionElementDto {
        private Long id;
        private String query;
        private String description;
        private String keyword;

    }

}
