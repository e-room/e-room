package com.project.Project.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    // 연관관계 설정하기

    // Oauth 회원번호
    private Long userId;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private String profileImageUrl;
}
