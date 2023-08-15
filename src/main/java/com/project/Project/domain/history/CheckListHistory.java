package com.project.Project.domain.history;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CheckListHistory extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long memberId;

    private Long buildingId;

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = true)
    private String memo;

    private Double score;

    @Column(nullable = true)
    private String lineNum;

    @Column(nullable = true)
    private String roomNum;

    @Column(nullable = true)
    private Double monthlyRent;

    @Column(nullable = true)
    private Double managementFee;

    @Column(nullable = true)
    private Double deposit;

    @Column(nullable = true)
    private Double netLeasableArea;

}
