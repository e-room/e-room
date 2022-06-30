package com.project.Project.domain;

import javax.persistence.Embeddable;

@Embeddable
public class AnonymousStatus {
    private String anonymousName;
    private Boolean isAnonymous;
}
