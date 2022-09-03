package com.project.Project.domain.review;

import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.Member;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.room.Room;
import com.project.Project.domain.embedded.AnonymousStatus;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@SQLDelete(sql = "UPDATE review SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "UniqueMemberAndRoom", columnNames = {"member_id", "room_id"})
    }
)
public class Review extends BaseEntity {

    @Id @GeneratedValue
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

    private Integer likeCnt;

    @OneToOne(fetch = FetchType.EAGER)
    private ReviewForm reviewForm;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ReviewToReviewCategory> reviewSummaryList = new ArrayList<>();

    @Embedded
    private AnonymousStatus anonymousStatus;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }

    // todo : 하드코딩 되어있는 필드 해결
    public ReviewResponseDto.ReviewListResponse toReviewListResponse() {
        return ReviewResponseDto.ReviewListResponse.builder()
                .profilePictureUrl("https://lh3.googleusercontent.com/ogw/AOh-ky20QeRrWFPI8l-q3LizWDKqBpsWTIWTcQa_4fh5=s64-c-mo")
                .nickName("하품하는 망아지")
                .score(new BigDecimal(4.5))
                .residencePeriod(reviewForm.getResidencePeriod())
                .floorHeight(reviewForm.getFloorHeight())
                .netLeasableArea(reviewForm.getNetLeasableArea())
                .deposit(reviewForm.getDeposit())
                .monthlyRent(reviewForm.getMonthlyRent())
                .managementFee(reviewForm.getManagementFee())
                .advantage(reviewForm.getAdvantageKeywordEnumList())
                .advantageDescription(reviewForm.getAdvantageDescription())
                .disadvantage(reviewForm.getDisadvantageKeywordEnumList())
                .disadvantageDescription(reviewForm.getDisadvantageDescription())
                .build();
    }
}
