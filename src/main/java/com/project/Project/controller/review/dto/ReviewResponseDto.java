package com.project.Project.controller.review.dto;

import com.project.Project.domain.enums.FloorHeight;
import com.project.Project.domain.enums.KeywordEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDto {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewListResponse {
        private String profilePictureUrl;
        private String nickName;
        private BigDecimal score;
        private Integer residencePeriod;
        private Integer residenceDuration;
        private FloorHeight floorHeight;
        private BigDecimal netLeasableArea;
        private Integer deposit;
        private Integer monthlyRent;
        private Integer managementFee;
        private List<KeywordEnum> advantage;
        private String advantageDescription;
        private List<KeywordEnum> disadvantage;
        private String disadvantageDescription;
        private Integer reviewLikeCnt;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewCreateResponse {
        private Long reviewId;
        private LocalDateTime createdAt;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewDeleteResponse {
        private Long reviewId;
        private LocalDateTime deletedAt;
        private Integer affectedRowCnt;
    }
}
