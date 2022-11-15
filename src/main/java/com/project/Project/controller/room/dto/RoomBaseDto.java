package com.project.Project.controller.room.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoomBaseDto {
    /**
     * 몇 동인지 : 101동
     */
    @PositiveOrZero
    private Integer lineNumber;

    /**
     * 몇 호인지 : 103호
     */
    @NotNull
    @PositiveOrZero
    private Integer roomNumber;
}
