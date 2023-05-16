package com.project.Project.service.admin.impl;

import com.project.Project.controller.admin.dto.AdminRequestDto;
import com.project.Project.domain.member.Member;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.serializer.admin.AdminSerializer;
import com.project.Project.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Member createMember(AdminRequestDto.CreateMemberDto request) {
        Member member = AdminSerializer.toMember(request);
        return memberRepository.save(member);
    }
}
