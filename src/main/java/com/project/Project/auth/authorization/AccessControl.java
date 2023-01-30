package com.project.Project.auth.authorization;

import com.project.Project.auth.enums.SubjectType;
import com.project.Project.domain.auth.Role;
import com.project.Project.domain.member.Member;

/**
 * AccessControl은 Policy, Subject로 구성됩니다.
 * AccessControl은 만들어진 Policy를 Role 또는 특정 멤버에 부여할 수 있고 validate할 수 있도록 abstract class로 만듭니다.
 * abstractClass에 있는 property들은 조건을 위한 값들입니다.
 * 뿐만 아니라 여러 조건에 따라 Policy를 구성할 수 있게끔 확장 구현 가능하게 설계할 것.
 * accessControl의 구현체는 전부 spring AOP로써 controller에 붙을 수 있다.
 * 구현체의 validate 함수는 member와 필요에 따라 다른 entity를 받으며 member에 달려있는 Role이 여러 개일 수 있으므로 Role의 우선순위에 따라 조건적으로 검사하도록 구현합니다.
 * EX) (Member,Role,Policy)
 * Ex) (null, User, policy1), (null, Admin, policy2)
 * Ex) (1, null, policy1)
 * Policy: Entity 전체나 특정 엔티티 instance 하나
 * Subject: Role 또는 특정 멤버 하나
 */
public abstract class AccessControl {

    private SubjectType subjectType;

    /**
     * Subject
     */
    private Member member;
    private Role role;

    private Policy policy;
}
