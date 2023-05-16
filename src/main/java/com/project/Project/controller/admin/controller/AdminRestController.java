package com.project.Project.controller.admin.controller;

import com.project.Project.controller.admin.dto.AdminRequestDto;
import com.project.Project.controller.admin.dto.AdminResponseDto;
import com.project.Project.domain.member.Member;
import com.project.Project.serializer.admin.AdminSerializer;
import com.project.Project.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final AdminService adminService;

    @PostMapping("/member")
    public ResponseEntity<AdminResponseDto.CreateMemberDto> createMember(@RequestBody AdminRequestDto.CreateMemberDto request) {
        Member createdMember = adminService.createMember(request);
        return ResponseEntity.ok(AdminSerializer.toCreateMemberDto(createdMember));
    }
}
