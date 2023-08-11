package com.project.Project.controller.checklist;

import com.project.Project.common.serializer.checklist.ChecklistSerializer;
import com.project.Project.controller.checklist.dto.ChecklistResponseDto;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.service.checklist.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checklists")
public class CheckListRestController {
    private final ChecklistService checklistService;


    @GetMapping("/{checklistId}")
    public ResponseEntity<ChecklistResponseDto.ChecklistElement> getChecklistAssessment(@PathVariable("checklistId") CheckList checklist) {
        ChecklistResponseDto.ChecklistElement checklistElement = ChecklistSerializer.toChecklistElement(checklist);
        return ResponseEntity.ok(checklistElement);
    }
}