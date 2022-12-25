package com.project.Project.service.member.impl;

import com.project.Project.domain.Member;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Optional<Member> findById(Long id) {
        return this.memberRepository.findById(id);
    }

    @Transactional
    public Long delete(Member member) {
        memberRepository.deleteById(member.getId());
        return member.getId();
    }
}
