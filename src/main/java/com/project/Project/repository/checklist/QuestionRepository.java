package com.project.Project.repository.checklist;

import com.project.Project.domain.checklist.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
