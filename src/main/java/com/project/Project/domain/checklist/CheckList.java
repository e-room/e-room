package com.project.Project.domain.checklist;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.eventListener.ChecklistListener;
import com.project.Project.domain.eventListener.ReviewListener;
import com.project.Project.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@EntityListeners(value = ChecklistListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CheckList extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "building_id")
    private Building building;

    @Column(nullable = true)
    private String nickname;

    @OneToMany(mappedBy = "checkList", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CheckListQuestion> checkListResponses = new ArrayList<>();

    @OneToMany(mappedBy = "checkList", cascade = CascadeType.ALL)
    private List<CheckListImage> checkListImageList = new ArrayList<>();

    @Column(length = 500)
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


    public void deleteAuthor() {
        this.author = null;
    }


    public void setAuthor(Member author) {
        if (this.author != null) { // 기존에 이미 팀이 존재한다면
            this.author.getCheckLists().remove(this); // 관계를 끊는다.
        }
        this.author = author;
        author.getCheckLists().add(this);
    }

    public void updateNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    public void updateLineNum(String lineNum) {
        if (lineNum != null) {
            this.lineNum = lineNum;
        }
    }

    public void updateRoomNum(String roomNum) {
        if (roomNum != null) {
            this.roomNum = roomNum;
        }
    }

    public void updateMonthlyRent(Double monthlyRent) {
        if (monthlyRent != null) {
            this.monthlyRent = monthlyRent;
        }
    }

    public void updateManagementFee(Double managementFee) {
        if (managementFee != null) {
            this.managementFee = managementFee;
        }
    }

    public void updateDeposit(Double deposit) {
        if (deposit != null) {
            this.deposit = deposit;
        }
    }

    public void updateNetLeasableArea(Double netLeasableArea) {
        if (netLeasableArea != null) {
            this.netLeasableArea = netLeasableArea;
        }
    }

    public void updateMemo(String memo) {
        if (memo != null) {
            this.memo = memo;
        }
    }

    public void updateScore(Double score) {
        if (score != null) {
            this.score = score;
        }
    }

    public void updateBuilding(Building building) {
        if(building != null) {
            this.building = building;
        }
    }
}
