package com.project.Project.repository.member;

import com.project.Project.domain.enums.AuthProviderType;
import com.project.Project.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndAuthProviderType(String email, AuthProviderType authProviderType);

    @Query(value = "select * from member where deleted = 1", nativeQuery = true)
    List<Member> findAllDeletedMember();

    List<Member> findByNickNameIsNull();

    Optional<Member> findByEmail(String email);
}
