package com.project.Project.domain.history;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.checklist.Question;
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
public class CheckListQuestionHistory extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long questionId;

    private Long checkListId;

    private String expression;

    public static CheckListQuestionHistory toCheckListQuestionHistory(CheckListQuestion checkListQuestion) {

        return CheckListQuestionHistory.builder()
                .questionId(checkListQuestion.getQuestion().getId())
                .checkListId(checkListQuestion.getCheckList().getId())
                .expression(checkListQuestion.getExpression().name())
                .build();
    }

}
