package com.project.Project.service.impl;

import com.project.Project.domain.Member;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return this.memberRepository.findById(id);
    }
}
