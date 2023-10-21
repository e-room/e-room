package com.project.Project.repository.checklist;

import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.checklist.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChecklistQuestionRepository extends JpaRepository<CheckListQuestion, Long> {
    Optional<CheckListQuestion> findByCheckList_IdAndQuestion_Id(Long checklistId, Long questionId);

    List<CheckListQuestion> findAllByCheckListId(Long checklistId);
}
