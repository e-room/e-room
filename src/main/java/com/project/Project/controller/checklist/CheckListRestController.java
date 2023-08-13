package com.project.Project.controller.checklist;

import com.project.Project.auth.AuthUser;
import com.project.Project.common.serializer.checklist.ChecklistSerializer;
import com.project.Project.controller.checklist.dto.ChecklistRequestDto;
import com.project.Project.controller.checklist.dto.ChecklistResponseDto;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.member.Member;
import com.project.Project.service.checklist.ChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Checklist API", description = "체크리스트 등록, 조회, 삭제")
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

    @PostMapping("/{checklistId}/checklistImages")
    public ResponseEntity<ChecklistResponseDto.ChecklistImageDto> postChecklistImage(@PathVariable("checklistId") CheckList checklist, @RequestPart MultipartFile checklistImage) {

        CheckListImage checkListImage = checklistService.saveChecklistImage(checklist, checklistImage);
        return ResponseEntity.ok(ChecklistSerializer.toChecklistImageDto(checkListImage));
    }

    @Operation(summary = "발품기록 단계1: 집정보 입력", description = "체크리스트 생성 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ChecklistResponseDto.ChecklistCreateDto.class)))
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("")
    public ResponseEntity<ChecklistResponseDto.ChecklistCreateDto> createChecklist(@RequestBody ChecklistRequestDto.ChecklistCreateDto request, @AuthUser Member member) {
        CheckList savedCheckList = checklistService.create(request, member);
        return ResponseEntity.ok(ChecklistSerializer.toChecklistCreateDto(savedCheckList));
    }

}