package com.project.Project.service.member;

import com.project.Project.domain.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);
}
