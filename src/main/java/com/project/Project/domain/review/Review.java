package com.project.Project.domain.review;

import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.Member;
import com.project.Project.domain.embedded.AnonymousStatus;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.review.ReviewEventListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NamedEntityGraphs({
        @NamedEntityGraph(name = "Review.withRoomAndBuilding", attributeNodes = {
                @NamedAttributeNode(value = "room", subgraph = "room")
        },
                subgraphs = @NamedSubgraph(name = "room", attributeNodes = {
                        @NamedAttributeNode("building")
                }))
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE review SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
@EntityListeners(value = ReviewEventListener.class)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueMemberAndRoom", columnNames = {"member_id", "room_id"})
        }
)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "review")
    @Builder.Default
    private List<ReviewLike> likeMemberList = new ArrayList<>();


    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewToReviewCategory> reviewToReviewCategoryList = new ArrayList<>();


    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewToReviewKeyword> reviewToReviewKeywordList = new ArrayList<>();

    @OneToOne(mappedBy = "review")
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

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImageList = new ArrayList<>();

    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }

    // todo : 하드코딩 되어있는 필드 해결
    public ReviewResponseDto.ReviewListResponse toReviewListResponse() {
        return ReviewResponseDto.ReviewListResponse.builder()
                .profilePictureUrl("https://lh3.googleusercontent.com/ogw/AOh-ky20QeRrWFPI8l-q3LizWDKqBpsWTIWTcQa_4fh5=s64-c-mo")
                .nickName("하품하는 망아지")
                .score(4.5)
                .residencePeriod(getResidenceDuration())
                .residenceDuration(getResidenceDuration())
                .netLeasableArea(getNetLeasableArea())
                .deposit(getDeposit())
                .monthlyRent(getMonthlyRent())
                .managementFee(getManagementFee())
                .advantage(getAdvantageKeywordEnumList())
                .advantageDescription(getAdvantageDescription())
                .disadvantage(getDisadvantageKeywordEnumList())
                .disadvantageDescription(getDisadvantageDescription())
                .build();
    }

    public List<KeywordEnum> getAdvantageKeywordEnumList() {
        return new ArrayList<>();
    }

    public List<KeywordEnum> getDisadvantageKeywordEnumList() {
        return new ArrayList<>();
    }
}
