package com.project.Project.service.member;

import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.member.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findByEmailAndAuthProviderType(String email, AuthProviderType authProviderType);

    Optional<Member> findById(Long id);

    Long delete(Member member);
}
