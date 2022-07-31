package com.project.Project.domain.room;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@SQLDelete(sql = "UPDATE room SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueBuildingAndRoomNumber", columnNames = {"building_id", "roomNumber"})
        }
)
public class Room extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "room")
    @Builder.Default
    private List<Review> reviewList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    /**
        몇 동인지
     **/
    @Column
    private Integer lineNumber;

    /**
     * 호실
     * 몇 호실인지
     */
    @NotNull
    @Column
    private Integer roomNumber;

    /**
     * 전용면적
     * 최대 유효 자릿수 : 10, 소수점 우측 자릿수 : 3
     */
    @Column(precision = 10, scale = 3)
    private BigDecimal netLeasableArea;

//    /**
//     * 빛 방향
//     * 남향/동향 등
//     */
//    @Enumerated(EnumType.STRING)
//    private LightDirection lightDirection;

    private Integer rentFee;

    private Integer managementFee;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }

    // TODO : 주변 상권(동영상) 관련 필드 추가 여부 질문
}
