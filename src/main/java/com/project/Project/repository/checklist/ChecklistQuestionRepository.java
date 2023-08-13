package com.project.Project.repository.checklist;

import com.project.Project.domain.checklist.CheckListQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistQuestionRepository extends JpaRepository<CheckListQuestion, Long> {
}
