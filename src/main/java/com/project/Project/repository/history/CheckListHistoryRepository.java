package com.project.Project.repository.history;

import com.project.Project.domain.history.CheckListHistory;
import com.project.Project.domain.history.ReviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckListHistoryRepository extends JpaRepository<CheckListHistory, Long> {
}
