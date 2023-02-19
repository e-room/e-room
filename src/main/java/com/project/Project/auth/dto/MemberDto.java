package com.project.Project.auth.dto;

import com.project.Project.domain.enums.AuthProviderType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String name;
    private String picture;
    private AuthProviderType authProviderType;

    @Builder
    public MemberDto(Long id, String email, String name, String picture, AuthProviderType authProviderType) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.authProviderType = authProviderType;
    }
}
