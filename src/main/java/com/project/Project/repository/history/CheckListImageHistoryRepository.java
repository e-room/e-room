package com.project.Project.repository.history;

import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.history.CheckListHistory;
import com.project.Project.domain.history.CheckListImageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckListImageHistoryRepository extends JpaRepository<CheckListImageHistory, Long> {
}
