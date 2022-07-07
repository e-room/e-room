package com.project.Project.domain.embedded;

import javax.persistence.Embeddable;

@Embeddable
public class AnonymousStatus {
    private String anonymousName;
    private Boolean isAnonymous;
}
