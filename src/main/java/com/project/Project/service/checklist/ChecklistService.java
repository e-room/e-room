package com.project.Project.service.checklist;

import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChecklistService {
    CheckList getChecklist(Long checklistId);

    CheckListImage saveChecklistImage(CheckList checklist, MultipartFile checklistImage);

    Long deleteChecklistImage(CheckList checklist, CheckListImage checklistImage);

    List<CheckListImage> getCheckListImage(CheckList checklist);
}
