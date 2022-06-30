package com.project.Project.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList = new LinkedList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberRoom> favoriteRoomList = new LinkedList<>();

    // Oauth 회원번호
    private Long userId;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private String profileImageUrl;
}
