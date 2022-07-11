package com.project.Project.config.auth.dto;

import com.project.Project.domain.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String profileImageUrl;

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.profileImageUrl = member.getProfileImageUrl();
    }
}