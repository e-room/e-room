package com.project.Project.controller.member.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.member.dto.MemberResponseDto;
import com.project.Project.domain.Member;
import com.project.Project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberService memberService;

    @DeleteMapping("/member/exit")
    public ResponseEntity<MemberResponseDto.MemberDeleteResponse> exitMember(@AuthUser Member loginMember) {
        return null;
    }
}
