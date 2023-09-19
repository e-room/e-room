package com.project.Project.controller.admin.dto;

import com.project.Project.domain.enums.AuthProviderType;
import lombok.Getter;

public class AdminRequestDto {

    @Getter
    public static class CreateMemberDto {
        private AuthProviderType authProviderType;
        private String email;
        private String name;
    }

    @Getter
    public static class LoginMemberDto {
        private String email;
    }
}
