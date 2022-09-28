package com.project.Project.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursorDto {
    private Long cursorId;
    private Integer size;
}
// todo : 이녀석 패키지 위치 어디에 두지? controller에 common을 파서 해야하나 util에 넣어야 하나