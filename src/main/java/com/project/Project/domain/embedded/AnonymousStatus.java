package com.project.Project.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class AnonymousStatus {
    private String anonymousName;
    private String anonymousProfilePicture;
    private Boolean isAnonymous;
}
