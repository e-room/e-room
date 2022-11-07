package com.project.Project.generator.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCase {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuildingId {
        private Long id;
    }

    private Long memberId;
    private List<BuildingId> buildingIds;
    
}