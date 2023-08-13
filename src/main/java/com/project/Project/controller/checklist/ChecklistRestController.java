package com.project.Project.controller.checklist;

import com.project.Project.common.serializer.checklist.ChecklistSerializer;
import com.project.Project.controller.checklist.dto.ChecklistResponseDto;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.service.checklist.ChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "체크리스트 조회", description = "체크리스트 조회 API<br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ChecklistResponseDto.ChecklistElement.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "checklistId", description = "조회하고자 하는 체크리스트의 id")
    })
    @GetMapping("/{checklistId}")
    public ResponseEntity<ChecklistResponseDto.ChecklistElement> getChecklistAssessment(@PathVariable("checklistId") CheckList checklist) {
        ChecklistResponseDto.ChecklistElement checklistElement = ChecklistSerializer.toChecklistElement(checklist);
        return ResponseEntity.ok(checklistElement);
    }

    @Operation(summary = "체크리스트 이미지 전송", description = "체크리스트용 이미지 업로드 API<br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ChecklistResponseDto.ChecklistImageDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "checklistId", description = "조회하고자 하는 체크리스트의 id", content = @Content(schema = @Schema(implementation = MultipartFile.class))),
    })
    @PostMapping("/{checklistId}/checklistImages")
    public ResponseEntity<ChecklistResponseDto.ChecklistImageDto> postChecklistImage(@PathVariable("checklistId") CheckList checklist, @RequestPart MultipartFile checklistImage) {

        CheckListImage checkListImage = checklistService.saveChecklistImage(checklist, checklistImage);
        return ResponseEntity.ok(ChecklistSerializer.toChecklistImageDto(checkListImage));
    }

    @Operation(summary = "체크리스트 이미지 삭제", description = "체크리스트용 이미지 삭제 API<br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ChecklistResponseDto.ChecklistImageDeleteDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "checklistId", description = "조회하고자 하는 체크리스트의 id"),
            @Parameter(name = "checklistImageId", description = "조회하고자 하는 체크리스트 이미지의 id"),
    })
    @DeleteMapping("/{checklistId}/checklistImages/{checklistImageId}")
    public ResponseEntity<ChecklistResponseDto.ChecklistImageDeleteDto> deleteChecklistImage(@PathVariable("checklistId") CheckList checklist, @PathVariable("checklistImageId") CheckListImage checklistImage) {
        Long checklistImageId = checklistService.deleteChecklistImage(checklist, checklistImage);
        List<CheckListImage> remainedImages = checklistService.getCheckListImage(checklist);

        return ResponseEntity.ok(ChecklistSerializer.toChecklistImageDeleteDto(checklistImageId, remainedImages));
    }
}