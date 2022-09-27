package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.enums.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@SQLDelete(sql = "UPDATE review_form SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class ReviewForm extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 거주 유형(아파트 / 오피스텔 또는 원룸 빌라 주택)
     */
    @Enumerated(EnumType.STRING)
    private ResidenceType residenceType;

    /**
     * 거주 기간 : 2018년 이전, 2018년까지, 2019년까지, ..., 2022년까지
     */
    @Enumerated(EnumType.STRING)
    private ResidencePeriod residencePeriod;

    /**
     * 거주층 : 저층, 중층, 고층
     */
    @Enumerated(EnumType.STRING)
    private FloorHeight floorHeight;

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
    private BigDecimal netLeasableArea;

    /**
     * 교통점수 : 5단계 선택
     */
    @Enumerated(EnumType.STRING)
    private ScoreOption trafficScore;

    /**
     * 건물 및 단지 점수 : 5단계 선택
     */
    @Enumerated(EnumType.STRING)
    private ScoreOption buildingComplexScore;

    /**
     * 주변 및 환경 점수 : 5단계 선택
     */
    @Enumerated(EnumType.STRING)
    private ScoreOption surroundingScore;

    /**
     * 내부 점수 : 5단계 선택
     */
    @Enumerated(EnumType.STRING)
    private ScoreOption internalScore;

    /**
     * 생활 및 입지 점수 : 5단계 선택
     */
    @Enumerated(EnumType.STRING)
    private ScoreOption livingLocationScore;

    /**
     * 장점 키워드 선택 : 없음 주차 대중교통 공원산책 치안 경비실 건물관리 분리수거 환기 방습
     * 단열 반려동물 키우기 방충 방음 엘레베이터 조용한동네 평지 마트/편의점 상가
     * (여러개 선택 가능)
     */
    @OneToMany(mappedBy = "reviewForm")
    @Builder.Default
    private List<AdvantageKeyword> advantageKeywordList = new ArrayList<>();

    private String advantageDescription;

    /**
     * 단점 키워드 선택 : 없음 주차 대중교통 공원산책 치안 경비실 건물관리 분리수거 환기 방습
     * 단열 반려동물 키우기 벌레 층간소음 엘레베이터 동네소음 언덕 마트/편의점 상가 학교/학원
     */
    @OneToMany(mappedBy = "reviewForm")
    @Builder.Default
    private List<DisadvantageKeyword> disadvantageKeywordList = new ArrayList<>();

    private String disadvantageDescription;

    /**
     * 해당 거주지 만족도 : 별 1개부터 5개까지 선택
     */
    private ScoreOption residenceSatisfaction;

    @OneToMany(mappedBy = "reviewForm")
    private List<ReviewImage> reviewImageList = new ArrayList<>();

    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }

    public List<AdvantageKeywordEnum> getAdvantageKeywordEnumList() {
        return this.advantageKeywordList.stream()
                .map(AdvantageKeyword::getAdvantageKeywordEnum)
                .collect(Collectors.toList());
    }

    public List<DisadvantageKeywordEnum> getDisadvantageKeywordEnumList() {
        return this.disadvantageKeywordList.stream()
                .map(DisadvantageKeyword::getDisadvantageKeywordEnum)
                .collect(Collectors.toList());
    }

}
