package com.project.Project.controller.review.dto;

import com.project.Project.domain.enums.DisadvantageKeywordEnum;
import com.project.Project.domain.enums.AdvantageKeywordEnum;
import com.project.Project.domain.enums.FloorHeight;
import com.project.Project.domain.enums.ResidencePeriod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDto {

    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class ReviewListResponse{
        private String profilePictureUrl;
        private String nickName;
        private BigDecimal score;
        private ResidencePeriod residencePeriod;
        private FloorHeight floorHeight;
        private BigDecimal netLeasableArea;
        private Integer deposit;
        private Integer monthlyRent;
        private Integer managementFee;
        private List<AdvantageKeywordEnum> advantage;
        private String advantageDescription;
        private List<DisadvantageKeywordEnum> disadvantage;
        private String disadvantageDescription;
    }

    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class ReviewCreateResponse{
        private Long reviewId;
        private LocalDateTime createdAt;
        private Integer affectedRowCnt;
    }
    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class ReviewDeleteResponse{
        private Long reviewId;
        private LocalDateTime deletedAt;
        private Integer affectedRowCnt;
    }
}
