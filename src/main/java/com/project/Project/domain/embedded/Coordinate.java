package com.project.Project.domain.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Embeddable
public class Coordinate {

    private Double latitude;

    private Double longitude;
}
