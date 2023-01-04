package com.project.Project.controller.member.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.member.dto.MemberRequestDto;
import com.project.Project.controller.member.dto.MemberResponseDto;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.member.RecentMapLocation;
import com.project.Project.serializer.member.MemberSerializer;
import com.project.Project.service.member.MemberService;
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

    @GetMapping("/member/profile")
    public ResponseEntity<MemberResponseDto.MemberProfileDto> getProfile(@AuthUser Member loginMember) {
        return ResponseEntity.ok(MemberSerializer.toMemberProfileDto(loginMember));
    }

    @DeleteMapping("/member/exit")
    public ResponseEntity<MemberResponseDto.MemberDeleteDto> exitMember(@AuthUser Member loginMember) {
        Long deletedMemberId = memberService.delete(loginMember);
        return ResponseEntity.ok(MemberSerializer.toMemberDeleteDto(deletedMemberId));
    }

    @PutMapping("/member/location")
    public ResponseEntity<MemberResponseDto.RecentMapLocationDto> updateLocation(@RequestBody MemberRequestDto.RecentMapLocationDto request, @AuthUser Member loginMember) {
        RecentMapLocation recentMapLocation = memberService.updateRecentMapLocation(request.getCoordinateDto(), loginMember);
        return ResponseEntity.ok(MemberSerializer.toRecentMapLocationDto(recentMapLocation));
    }
}
