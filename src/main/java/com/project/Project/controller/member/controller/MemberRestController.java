package com.project.Project.controller.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Project.auth.AuthUser;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.checklist.ChecklistException;
import com.project.Project.common.exception.review.ReviewException;
import com.project.Project.common.serializer.checklist.ChecklistSerializer;
import com.project.Project.controller.checklist.dto.ChecklistResponseDto;
import com.project.Project.controller.member.dto.MemberRequestDto;
import com.project.Project.controller.member.dto.MemberResponseDto;

import com.project.Project.domain.checklist.CheckList;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.member.RecentMapLocation;
import com.project.Project.common.serializer.member.MemberSerializer;
import com.project.Project.service.checklist.ChecklistService;
import com.project.Project.service.member.MemberService;
import com.project.Project.common.util.component.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.project.Project.auth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.IS_LOCAL;

@Tag(name = "Member API", description = "내 정보 조회, 회원 탈퇴")
@RestController
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberService memberService;
    private final ChecklistService checklistService;
    private final ObjectMapper mapper;

    @Operation(summary = "내 정보 조회 [6]", description = "내 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MemberResponseDto.MemberProfileDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
        @Parameter(name = "loginMember", hidden = true)
    })
    @GetMapping("/member/profile")
    public ResponseEntity<MemberResponseDto.MemberProfileDto> getProfile(@AuthUser Member loginMember) {
        return ResponseEntity.ok(MemberSerializer.toMemberProfileDto(loginMember));
    }

    @Operation(summary = "회원 탈퇴 [6]", description = "탈퇴하기 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MemberResponseDto.MemberDeleteDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "loginMember", hidden = true),
            @Parameter(name = "request", hidden = true),
            @Parameter(name = "response", hidden = true)
    })
    @DeleteMapping("/member/exit")
    public void exitMember(@AuthUser Member loginMember, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Boolean isLocal = CookieUtil.getCookie(request, IS_LOCAL)
                .map(Cookie::getValue)
                .map(Boolean::parseBoolean).orElse(false);

        ResponseCookie accessTokenCookie = CookieUtil.createDeleteTokenCookie("accessToken", isLocal);
        ResponseCookie refreshTokenCookie = CookieUtil.createDeleteTokenCookie("refreshToken", isLocal);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        Long deletedMemberId = memberService.delete(loginMember);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(mapper.writeValueAsString(MemberSerializer.toMemberDeleteDto(deletedMemberId)));
    }

    /**
     * @deprecated 마지막 위치 저장을 클라이언트 단에서 저장하면서 쓰이지 않게 되었습니다.
     */
    @Deprecated(since = "")
    @Operation(summary = "마지막 지도 위치 저장 [3.0.1]", description = "마지막으로 조회한 지도의 중심 저장 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = MemberResponseDto.RecentMapLocationDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "loginMember", hidden = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "지도 위치 저장 DTO", required = true,
            content = @Content(schema = @Schema(implementation = MemberRequestDto.RecentMapLocation.class)))
    @PutMapping("/member/location")
    public ResponseEntity<MemberResponseDto.RecentMapLocationDto> updateLocation(
            @RequestBody MemberRequestDto.RecentMapLocation request, @AuthUser Member loginMember) {
        RecentMapLocation recentMapLocation = memberService.updateRecentMapLocation(request.getCoordinateDto(), loginMember);
        return ResponseEntity.ok(MemberSerializer.toRecentMapLocationDto(recentMapLocation));
    }

    @Operation(summary = "체크리스트 삭제", description = "체크리스트 삭제 API<br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ChecklistResponseDto.ChecklistDeleteDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "memberId", description = "삭제하고자 하는 체크리스트의 유저 id"),
            @Parameter(name = "checklistId", description = "삭제하고자 하는 체크리스트의 id")
    })
    @DeleteMapping("/member/{memberId}/checklists/{checklistId}")
    public ResponseEntity<ChecklistResponseDto.ChecklistDeleteDto> deleteChecklist(@PathVariable("memberId") Long memberId, @PathVariable("checklistId") Long checklistId, @AuthUser Member loginMember) {
        CheckList checklist = checklistService.getChecklist(checklistId);
        if (!checklist.getAuthor().getId().equals(loginMember.getId())) {
            throw new ChecklistException("다른 사람의 체크리스트를 삭제할 수 없습니다.", ErrorCode.CHECKLIST_ACCESS_DENIED);
        }
        Long deletedChecklistId = checklistService.deleteById(checklistId);
        return ResponseEntity.ok(ChecklistSerializer.toChecklistDeletedDto(deletedChecklistId));
    }
}
