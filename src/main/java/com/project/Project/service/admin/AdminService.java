package com.project.Project.service.admin;

import com.project.Project.controller.admin.dto.AdminRequestDto;
import com.project.Project.domain.member.Member;

public interface AdminService {

    Member createMember(AdminRequestDto.CreateMemberDto request);
}
