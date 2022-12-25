package com.project.Project.controller.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberResponseDto {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberDeleteDto {
        private Long memberId;
        private LocalDateTime deletedAt;
    }
}
