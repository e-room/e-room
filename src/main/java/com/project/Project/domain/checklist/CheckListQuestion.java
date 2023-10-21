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

    @Column(length = 200)
    private String memo;

    public void setCheckList(CheckList checkList) {
        if(this.checkList != null) {
            this.checkList.getCheckListResponses().remove(this);
        }
        this.checkList = checkList;
        checkList.getCheckListResponses().add(this);
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void updateExpression(Expression expression) {
        if (expression != null) {
            this.expression = expression;
        }
    }

    public void updateMemo(String memo) {
        if (memo != null) {
            this.memo = memo;
        }
    }
}
