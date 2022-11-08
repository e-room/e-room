package com.project.Project.validator.interaction;

import com.project.Project.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class FavoriteExistVO {
    private final Member member;
    private final Long buildingId;

    public FavoriteExistVO(Member member, Long buildingId) {
        this.member = member;
        this.buildingId = buildingId;
    }
}
