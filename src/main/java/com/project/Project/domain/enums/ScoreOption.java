package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum ScoreOption {
    VERY_SATISFIED("매우 만족", 5),
    SATISFIED("만족", 4),
    AVERAGE("보통", 3),
    DISSATISFIED("불만족", 2),
    VERY_DISSATISFIED("매우 불만족", 1);

    private String description;
    private int score; // 추후, 점수 등을 보여줄 가능성을 대비

    ScoreOption(String description, int score) {
        this.description = description;
        this.score = score;
    }
}
