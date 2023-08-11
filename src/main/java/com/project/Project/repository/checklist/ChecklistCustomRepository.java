package com.project.Project.repository.checklist;

import com.project.Project.domain.checklist.CheckListImage;

import java.util.List;

public interface ChecklistCustomRepository {

    Long countByChecklistId(Long checklistId);

    List<CheckListImage> getCheckListImagesWithLock(Long checklistId);
}
