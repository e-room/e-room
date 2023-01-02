package com.project.Project.controller.member.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.member.dto.MemberResponseDto;
import com.project.Project.domain.member.Member;
import com.project.Project.serializer.member.MemberSerializer;
import com.project.Project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
}
