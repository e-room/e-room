package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum ResidencePeriod {
    // 거주 기간 : 2018년 이전, 2018년까지, 2019년까지, ..., 2022년까지
    BEFORE_EIGHTEEN("2018년이전"),
    UNTIL_EIGHTEEN("2018년까지"),
    UNTIL_NINETEEN("2019년까지"),
    UNTIL_TWENTY("2020년까지"),
    UNTIL_TWENTY_ONE("2021년까지"),
    UNTIL_TWENTY_TWO("2022년까지");

    private String description;

    ResidencePeriod(String description) {
        this.description = description;
    }
}
