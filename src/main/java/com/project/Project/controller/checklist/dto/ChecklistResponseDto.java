package com.project.Project.controller.checklist.dto;

import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.controller.room.dto.RoomDto;
import com.project.Project.domain.checklist.CheckListQuestion;
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
}
