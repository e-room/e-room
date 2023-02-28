package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.AnonymousStatus;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.eventListener.ReviewListener;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.member.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@NamedEntityGraphs({
        @NamedEntityGraph(name = "Review.withBuilding", attributeNodes = {
                @NamedAttributeNode(value = "building")
        }
                ),
        @NamedEntityGraph(name = "Review.withReviewCategory", attributeNodes = {
                @NamedAttributeNode(value = "reviewToReviewCategoryList", subgraph = "reviewCategory")
        },
                subgraphs = @NamedSubgraph(name = "reviewCategory", attributeNodes = {
                        @NamedAttributeNode("reviewCategory")
                }))
})
@EntityListeners(value = ReviewListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueMemberAndBuilding", columnNames = {"member_id", "building_id"})
        }
)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @OneToMany(mappedBy = "review")
    @Builder.Default
    private List<ReviewLike> likeMemberList = new ArrayList<>();


    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewToReviewCategory> reviewToReviewCategoryList = new ArrayList<>();


    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewToReviewKeyword> reviewToReviewKeywordList = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewLike> reviewLikeList = new ArrayList<>();

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL)
    private ReviewSummary reviewSummary;

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

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImageList = new ArrayList<>();

    public Optional<ReviewToReviewCategory> getReviewCategory(ReviewCategoryEnum type) {
        return this.reviewToReviewCategoryList.stream()
                .filter(reviewToReviewCategory -> {
                    ReviewCategoryEnum reviewCategoryEnum = Optional.ofNullable(reviewToReviewCategory.getReviewCategory())
                            .map(ReviewCategory::getType)
                            .orElse(null);
                    if (reviewCategoryEnum != null) {
                        return reviewCategoryEnum.equals(type);
                    }
                    return false;
                })
                .findFirst();
    }

    /**
     * Member 엔티티와의 연관관계를 끊는 메소드
     */
    public void deleteAuthor() {
        this.author = null;
    }

    public void setAuthor(Member author) {
        if (this.author != null) { // 기존에 이미 팀이 존재한다면
            this.author.getReviewList().remove(this); // 관계를 끊는다.
        }
        this.author = author;
        author.getReviewList().add(this);
    }
}
