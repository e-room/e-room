package com.project.Project.controller.checklist;

import com.project.Project.common.serializer.checklist.ChecklistSerializer;
import com.project.Project.controller.checklist.dto.ChecklistResponseDto;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.service.checklist.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checklists")
public class ChecklistRestController {
    private final ChecklistService checklistService;


    @GetMapping("/{checklistId}")
    public ResponseEntity<ChecklistResponseDto.ChecklistElement> getChecklistAssessment(@PathVariable("checklistId") CheckList checklist) {
        ChecklistResponseDto.ChecklistElement checklistElement = ChecklistSerializer.toChecklistElement(checklist);
        return ResponseEntity.ok(checklistElement);
    }

    @PostMapping("/{checklistId}/checklistImages")
    public ResponseEntity<ChecklistResponseDto.ChecklistImageDto> postChecklistImage(@PathVariable("checklistId") CheckList checklist, @RequestPart MultipartFile checklistImage) {

        CheckListImage checkListImage = checklistService.saveChecklistImage(checklist, checklistImage);
        return ResponseEntity.ok(ChecklistSerializer.toChecklistImageDto(checkListImage));
    }

    @DeleteMapping("/{checklistId}/checklistImages/{checklistImageId}")
    public ResponseEntity<ChecklistResponseDto.ChecklistImageDeleteDto> deleteChecklistImage(@PathVariable("checklistId") CheckList checklist, @PathVariable("checklistImageId") CheckListImage checklistImage) {
        Long checklistImageId = checklistService.deleteChecklistImage(checklist, checklistImage);
        List<CheckListImage> remainedImages = checklistService.getCheckListImage(checklist);

        return ResponseEntity.ok(ChecklistSerializer.toChecklistImageDeleteDto(checklistImageId, remainedImages));
    }
}