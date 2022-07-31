package com.project.Project.controller.review.dto;

import com.project.Project.domain.review.DisadvantageKeyword;
import com.project.Project.domain.enums.AdvantageKeywordEnum;
import com.project.Project.domain.enums.FloorHeight;
import com.project.Project.domain.enums.ResidencePeriod;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDto {

    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class ReviewListResponse{
        private String profilePictureUrl;
        private String nickName;
        private ResidencePeriod residencePeriod;
        private FloorHeight floorHeight;
        private Integer deposit;
        private Integer monthlyRent;
        private String managementFeeDescription;
        private Integer score;
        private List<AdvantageKeywordEnum> advantage;
        private String advantageTranslation;
        private List<DisadvantageKeyword> disadvantage;
        private String disadvantageTranslation;
    }

    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class ReviewCreateResponse{
        private List<Integer> reviewId;
        private LocalDateTime createdAt;
        private Integer affectedRowCnt;
    }
    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class ReviewDeleteResponse{
        private Integer reviewId;
        private LocalDateTime createdAt;
        private Integer affectedRowCnt;
    }
}
