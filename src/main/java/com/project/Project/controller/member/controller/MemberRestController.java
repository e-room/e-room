package com.project.Project.controller.member.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.member.dto.MemberResponseDto;
import com.project.Project.domain.member.Member;
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
        return ResponseEntity.ok(MemberResponseDto.MemberProfileDto.builder()
                .name(loginMember.getName())
                .email(loginMember.getEmail())
                .profileImageUrl(loginMember.getProfileImage().getUrl())
                .build());
    }

    @DeleteMapping("/member/exit")
    public ResponseEntity<MemberResponseDto.MemberDeleteDto> exitMember(@AuthUser Member loginMember) {
        LocalDateTime now = LocalDateTime.now();
        Long deletedMemberId = memberService.delete(loginMember);
        // todo : 빌더 호출해서 만들게 되는 거 팩토리 패턴으로 리팩토링
        return ResponseEntity.ok(MemberResponseDto.MemberDeleteDto.builder()
                .memberId(deletedMemberId)
                .deletedAt(now)
                .build());
    }
}
