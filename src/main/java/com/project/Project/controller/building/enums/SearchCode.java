package com.project.Project.controller.building.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum SearchCode {

    ADDRESS("address", "주소"),
    NAME("name", "건물명");

    private final String en;
    private final String kr;

    SearchCode(String en, String kr) {
        this.en = en;
        this.kr = kr;
    }
}
