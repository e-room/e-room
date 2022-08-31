package com.project.Project.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Embeddable
public class AnonymousStatus {
    private String anonymousName;
    private Boolean isAnonymous;

    public static AnonymousStatus generateAnonymousStatus() {
        // todo : 형용사 + 명사의 이름 짓는 로직 포함
        return AnonymousStatus.builder()
                .anonymousName("하품하는 망아지")
                .isAnonymous(Boolean.TRUE)
                .build();
    }
}
