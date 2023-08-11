package com.project.Project.controller.checklist.dto;

import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.controller.room.dto.RoomDto;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.checklist.CheckListQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
        private String memo;
        private Double score;
        private List<CheckListImage> checkListImages;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ChecklistElement extends ReviewResponseDto.BasicAssessmentDto {
        @Nullable
        private AddressDto address;
        private RoomDto.RoomBaseDto room;
        private ChecklistAssessment assessment;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
