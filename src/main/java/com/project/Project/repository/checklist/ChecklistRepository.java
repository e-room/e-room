package com.project.Project.repository.checklist;

import com.project.Project.domain.checklist.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistRepository extends JpaRepository<CheckList, Long> {
}
