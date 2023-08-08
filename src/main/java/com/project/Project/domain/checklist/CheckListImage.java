package com.project.Project.domain.checklist;


import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.Uuid;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CheckListImage extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private String url;

    @OneToOne
    @JoinColumn(name = "uuid_id")
    private Uuid uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "checklist_id")
    private CheckList checkList;


    @PreRemove
    public void deleteHandler() {
        this.checkList = null;
        super.setDeleted(true);
    }

    public void setReview(CheckList checkList) {
        if (this.checkList != null) { // 기존에 이미 팀이 존재한다면
            this.checkList.getCheckListImageList().remove(this); // 관계를 끊는다.
        }
        this.checkList = checkList;
        checkList.getCheckListImageList().add(this);
    }
}
