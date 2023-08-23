package com.project.Project.repository.checklist;

import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<CheckList, Long> {
    List<CheckList> findAllByAuthorId(Long memberId);

    List<CheckList> findAllByBuildingId(Long building);
}
