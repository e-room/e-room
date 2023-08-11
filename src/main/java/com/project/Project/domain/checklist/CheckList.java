package com.project.Project.domain.checklist;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.building.Building;
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

}
