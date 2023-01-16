package com.project.Project.auth.dto;

import com.project.Project.domain.enums.AuthProviderType;
import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UidDto {
    private String email;
    private AuthProviderType authProviderType;
}
