package com.project.Project.repository.member;

import com.project.Project.auth.enums.MemberRole;
import com.project.Project.domain.auth.Role;
import com.project.Project.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findRoleByMemberRoleIn(List<MemberRole> roleEnum);

    List<Role> findRoleByMember(Member member);

}
