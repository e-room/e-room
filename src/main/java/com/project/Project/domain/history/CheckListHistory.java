package com.project.Project.domain.history;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.Review;
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

    private Long checkListId;

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

    public static CheckListHistory toCheckListHistory(CheckList checkList) {
        return CheckListHistory.builder()
                .checkListId(checkList.getId())
                .memberId(checkList.getAuthor().getId())
                .buildingId(checkList.getBuilding() == null ? null : checkList.getBuilding().getId())
                .nickname(checkList.getNickname())
                .memo(checkList.getMemo())
                .score(checkList.getScore())
                .lineNum(checkList.getLineNum())
                .roomNum(checkList.getRoomNum())
                .monthlyRent(checkList.getMonthlyRent())
                .managementFee(checkList.getManagementFee())
                .deposit(checkList.getDeposit())
                .netLeasableArea(checkList.getNetLeasableArea())
                .build();
    }

}
