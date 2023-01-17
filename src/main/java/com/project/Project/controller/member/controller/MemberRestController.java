package com.project.Project.controller.member.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.member.dto.MemberRequestDto;
import com.project.Project.controller.member.dto.MemberResponseDto;

import com.project.Project.domain.member.Member;
import com.project.Project.domain.member.RecentMapLocation;
import com.project.Project.serializer.member.MemberSerializer;
import com.project.Project.service.member.MemberService;
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

import java.time.LocalDateTime;

@Tag(name = "Member API", description = "내 정보 조회, 회원 탈퇴")
@RestController
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberService memberService;

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
            @Parameter(name = "loginMember", hidden = true)
    })
    @DeleteMapping("/member/exit")
    public ResponseEntity<MemberResponseDto.MemberDeleteDto> exitMember(@AuthUser Member loginMember) {
        Long deletedMemberId = memberService.delete(loginMember);
        return ResponseEntity.ok(MemberSerializer.toMemberDeleteDto(deletedMemberId));
    }

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
}
