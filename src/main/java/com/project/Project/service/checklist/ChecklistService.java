package com.project.Project.service.checklist;

import com.project.Project.controller.checklist.dto.ChecklistRequestDto;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.member.Member;
import org.springframework.web.multipart.MultipartFile;

public interface ChecklistService {
    CheckList getChecklist(Long checklistId);

    CheckListImage saveChecklistImage(CheckList checklist, MultipartFile checklistImage);

    CheckList create(ChecklistRequestDto.ChecklistCreateDto request, Member member);
}
