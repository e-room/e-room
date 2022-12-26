package com.project.Project.repository.member;

import com.project.Project.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query(value = "select * from member where deleted = 1", nativeQuery = true)
    List<Member> findAllDeletedMember();
}
