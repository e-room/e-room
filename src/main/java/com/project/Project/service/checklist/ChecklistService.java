package com.project.Project.service.checklist;

import com.project.Project.controller.checklist.dto.ChecklistRequestDto;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.member.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChecklistService {
    CheckList getChecklist(Long checklistId);

    CheckListImage saveChecklistImage(CheckList checklist, MultipartFile checklistImage);

    Long deleteChecklistImage(CheckList checklist, CheckListImage checklistImage);

    List<CheckListImage> getCheckListImage(CheckList checklist);

    CheckList create(ChecklistRequestDto.ChecklistCreateDto request, Member member);

    Long deleteById(Long checklistId);

    CheckListQuestion updateChecklistQuestion(Long checklistId, Long questionId, ChecklistRequestDto.ChecklistQuestionUpdateDto request, Member member);

    List<CheckList> getUserCheckList(Long memberId);
}
