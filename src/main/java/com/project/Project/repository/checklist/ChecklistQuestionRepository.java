package com.project.Project.repository.checklist;

import com.project.Project.domain.checklist.CheckListQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChecklistQuestionRepository extends JpaRepository<CheckListQuestion, Long> {
    Optional<CheckListQuestion> findByCheckList_IdAndQuestion_Id(Long checklistId, Long questionId);
}
