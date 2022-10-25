package com.project.Project.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDto {
    private String email;
    private String name;
    private String picture;

    @Builder
    public MemberDto(String email, String name, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
}
