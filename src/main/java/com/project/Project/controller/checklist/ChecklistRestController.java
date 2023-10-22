package com.project.Project.controller.checklist;

import com.project.Project.auth.AuthUser;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.review.ReviewException;
import com.project.Project.common.serializer.checklist.ChecklistSerializer;
import com.project.Project.common.serializer.review.ReviewSerializer;
import com.project.Project.common.validator.ExistReview;
import com.project.Project.controller.checklist.dto.ChecklistRequestDto;
import com.project.Project.controller.checklist.dto.ChecklistResponseDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.checklist.CheckListImage;
import com.project.Project.domain.checklist.CheckListQuestion;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.Review;
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

import java.util.ArrayList;
import java.util.List;


@Tag(name = "Checklist API", description = "체크리스트 등록, 조회, 삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("/checklists")
public class ChecklistRestController {
    private final ChecklistService checklistService;

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Operation(summary = "발품기록 단계2: 체크리스트 체킹", description = "체크리스트 질문지 표현(좋아요/별로에요) 업데이트 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ChecklistResponseDto.ChecklistQuestionUpdateDto.class)))
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PatchMapping("/{checklistId}/questions/{questionId}")
    public ResponseEntity<ChecklistResponseDto.ChecklistQuestionUpdateDto> updateChecklistQuestion(@PathVariable("checklistId") Long checklistId,
                                                                                                   @PathVariable("questionId") Long questionId,
                                                                                                   @RequestBody ChecklistRequestDto.ChecklistQuestionUpdateDto request,
                                                                                                   @AuthUser Member member) {
        CheckListQuestion updatedChecklistQuestion = checklistService.updateChecklistQuestion(checklistId, questionId, request, member);
        return ResponseEntity.ok(ChecklistSerializer.toChecklistQuestionUpdateDto(updatedChecklistQuestion));
    }

    @Operation(summary = "명세하기", description = "...")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ChecklistResponseDto.ChecklistUpdateDto.class)))
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PatchMapping("/checklists/{checklistId}")
    public ResponseEntity<ChecklistResponseDto.ChecklistUpdateDto> updateChecklist(@PathVariable("checklistId") Long checklistId,
                                                                                   @RequestBody ChecklistRequestDto.ChecklistUpdateDto request) {
        return null;
    }

    @Deprecated
    @Operation(summary = "체크리스트 질문 리스트 조회", description = "체크리스트 질문 리스트 조회 API<br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "checklistId", description = "조회하고자 하는 체크리스트의 id")
    })
    @GetMapping("/{checklistId}/questions")
    public ResponseEntity<List<ChecklistResponseDto.QuestionElementDto>> getChecklistQuestions(@PathVariable("checklistId") Long checklistId) {
        List<ChecklistResponseDto.QuestionElementDto> checklistQuestions = checklistService.getChecklistQuestions(checklistId);
        return ResponseEntity.ok(checklistQuestions);
    }


}