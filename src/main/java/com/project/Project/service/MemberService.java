package com.project.Project.service;

import com.project.Project.domain.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);
}
