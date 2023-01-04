package com.project.Project.controller.review.dto;

import com.project.Project.auth.dto.MemberDto;
import com.project.Project.controller.room.dto.RoomResponseDto;
import com.project.Project.domain.Uuid;
import com.project.Project.domain.enums.KeywordEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDto {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewListDto {
        private BaseReviewDto baseReviewDto;
        private RoomResponseDto.BaseRoomDto baseRoomDto;
        private ReviewScoreDto reviewScoreDto;
        private MemberDto authorDto;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class BaseReviewDto {
        private Long reviewId;
        private LocalDateTime createdAt;
        private Double score;
        private Integer residencePeriod;
        private Integer residenceDuration;
        private Double netLeasableArea;
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
    public static class ReviewScoreDto {
        private Double traffic;

        private Double buildingComplex;

        private Double surrounding;

        private Double internal;

        private Double livingLocation;

        private Double residenceSatisfaction;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewCreateDto {
        private Long reviewId;
        private LocalDateTime createdAt;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewDeleteDto {
        private Long reviewId;
        private LocalDateTime deletedAt;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewImageDto {
        private String uuid;
        private String url;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewImageListDto {
        private List<ReviewImageDto> reviewImageList;
        private Integer reviewImageCount;
    }
}
