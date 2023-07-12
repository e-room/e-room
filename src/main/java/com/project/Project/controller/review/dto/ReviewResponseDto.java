package com.project.Project.controller.review.dto;

import com.project.Project.auth.dto.MemberDto;
import com.project.Project.controller.member.dto.MemberResponseDto;
import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.service.review.ReviewService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewResponseDto {
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewSliceDto {
        private Slice<ReviewDto> reviewSlicedList;
        private Boolean needToBlur;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewListDto {
        private List<BestReviewDto> reviewDtoList;
        private Integer size;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class BestReviewDto {
        private AddressDto address;
        private String buildingName;
        private ReviewBaseDto reviewBaseDto;
        private ReviewScoreDto reviewScoreDto;
        private MemberDto authorDto;
        private ReviewImageListDto reviewImageListDto;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewDto {
        private ReviewBaseDto reviewBaseDto;
        private ReviewScoreDto reviewScoreDto;
        private MemberDto authorDto;
        @Builder.Default
        private Boolean isLiked = false;

        public void setIsLiked(Boolean recommended) {
            this.isLiked = recommended;
        }
        private ReviewImageListDto reviewImageListDto;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewBaseDto {
        private Long reviewId;
        private LocalDateTime createdAt;
        private Double score;
        private Integer residenceStartYear;
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
        private Long buildingId;
        private Boolean isFirstReview;
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


    @NoArgsConstructor
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ReviewReadByMemberDto {
        private MemberResponseDto.MemberProfileDto memberProfileDto;
        private List<Long> reviewIds;
    }

    public static enum ReviewExposeState {
        NONE,
        ONE,
        ONLY_READ,
        ALL
    }

    public static abstract class ReviewExposeCommand {
        public ReviewExposeState state;

        public abstract List<ReviewResponseDto.ReviewDto> execute();
    }

    public static class NoneReviewExposeCommand extends ReviewExposeCommand {

        private List<ReviewResponseDto.ReviewDto> originReviewList;

        public NoneReviewExposeCommand(List<ReviewDto> originReviewList) {
            this.originReviewList = originReviewList;
        }

        @Override
        public List<ReviewResponseDto.ReviewDto> execute() {
            return new ArrayList<>();
        }
    }

    public static class OneReviewExposeCommand extends ReviewExposeCommand {

        private List<ReviewResponseDto.ReviewDto> originReviewList;

        public OneReviewExposeCommand(List<ReviewDto> originReviewList) {
            this.originReviewList = originReviewList;
        }

        @Override
        public List<ReviewResponseDto.ReviewDto> execute() {
            if (this.originReviewList.size() > 1) {
                return this.originReviewList.subList(0, 1);
            } else {
                return this.originReviewList;
            }
        }
    }

    public static class AllReviewExposeCommand extends ReviewExposeCommand {

        private List<ReviewResponseDto.ReviewDto> originReviewList;

        public AllReviewExposeCommand(List<ReviewDto> originReviewList) {
            this.originReviewList = originReviewList;
        }

        @Override
        public List<ReviewResponseDto.ReviewDto> execute() {
            return originReviewList;
        }
    }

    public static class OnlyReadReviewExposeCommand extends ReviewExposeCommand {

        private List<ReviewResponseDto.ReviewDto> originReviewList;
        private Long memberId;
        private Long buildingId;
        private ReviewService reviewService;

        public OnlyReadReviewExposeCommand(List<ReviewDto> originReviewList, Long memberId, Long buildingId, ReviewService reviewService) {
            this.originReviewList = originReviewList;
            this.memberId = memberId;
            this.buildingId = buildingId;
            this.reviewService = reviewService;
        }

        @Override
        public List<ReviewResponseDto.ReviewDto> execute() {
            List<Long> reviewReadIds = this.reviewService.getReadReviews(this.memberId, this.buildingId).stream().map(reviewRead -> reviewRead.getReview().getId()).collect(Collectors.toList());
            List<ReviewResponseDto.ReviewDto> reviewResponseList = originReviewList.stream().filter(originReview -> reviewReadIds.contains(originReview.getReviewBaseDto().getReviewId())).collect(Collectors.toList());
            return reviewResponseList;
        }
    }
}
