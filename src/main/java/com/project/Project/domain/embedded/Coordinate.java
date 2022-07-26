package com.project.Project.domain.embedded;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Coordinate {

    private Double latitude;

    private Double longitude;
}
