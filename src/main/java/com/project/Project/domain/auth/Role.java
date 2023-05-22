package com.project.Project.domain.auth;

import com.project.Project.auth.enums.MemberRole;
import com.project.Project.domain.member.Member;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Role과 Policy는 AccessControl을 통해 N:N 관계를 가집니다.
 * 한 사람은 여러 개의 Role을 가질 수 있습니다.
 * 이 때 각 Role에서 상충되는 Policy에 대한 권한 결정을 하기 위해 order라는 column을 role에 만듭니다.
 * PolicyManager는 User의 Role을 들고 온 후, order로 정렬해서 resource에 대한 Action 정책을 만듭니다. order가 낮을수록 높은 우선순위를 가집니다.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
    
    @Override
    public String getAuthority() {
        return "ROLE_" + this.memberRole.name().toUpperCase();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        if (this.member != null) { // 기존에 이미 팀이 존재한다면
            this.member.getRoles().remove(this); // 관계를 끊는다.
        }
        this.member = member;
        member.getRoles().add(this);
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id", referencedColumnName = "id")
//    public Role parent;
//
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
//    public List<Role> childRoles;
}

