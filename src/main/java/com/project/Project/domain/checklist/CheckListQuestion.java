package com.project.Project.domain.checklist;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.enums.Expression;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CheckListQuestion extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checklist_id")
    private CheckList checkList;

    @Enumerated(EnumType.STRING)
    private Expression expression;

    public void setCheckList(CheckList checkList) {
        if(this.checkList != null) {
            this.checkList.getCheckListResponses().remove(this);
        }
        this.checkList = checkList;
        checkList.getCheckListResponses().add(this);
    }
}
