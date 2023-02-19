package com.project.Project.domain.history;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.embedded.AnonymousStatus;
import com.project.Project.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ReviewHistory extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long reviewId;

    private Long memberId;

    private Long buildingId;

    @Embedded
    private AnonymousStatus anonymousStatus;

    /**
     * 거주 시작 년도 : 거주 시작년도
     */
    private Integer residenceStartYear;

    /**
     * 거주 기간: 개월 수
     */
    private Integer residenceDuration;


    /**
     * 보증금 : 000만원
     */
    private Integer deposit;

    /**
     * 월세 : 00만원
     */
    private Integer monthlyRent;

    /**
     * 관리비 : 몇호기준 얼마정도에요. 여름에는 에어컨을 틀면 추가적으로 ....
     */
    private Integer managementFee;

    /**
     * 전용면적
     * 최대 유효 자릿수 : 10, 소수점 우측 자릿수 : 3
     */
    @Column(precision = 10, scale = 3)
    private Double netLeasableArea;

    private String advantageDescription;

    private String disadvantageDescription;


    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }

    public static ReviewHistory toReviewHistory(Review review) {
        return ReviewHistory.builder()
                .reviewId(review.getId())
                .buildingId(review.getBuilding().getId())
                .memberId(review.getAuthor().getId())
                .anonymousStatus(review.getAnonymousStatus())
                .residenceStartYear(review.getResidenceStartYear())
                .residenceDuration(review.getResidenceDuration())
                .deposit(review.getDeposit())
                .monthlyRent(review.getMonthlyRent())
                .managementFee(review.getManagementFee())
                .netLeasableArea(review.getNetLeasableArea())
                .advantageDescription(review.getAdvantageDescription())
                .disadvantageDescription(review.getDisadvantageDescription())
                .build();
    }
}
