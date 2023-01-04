package com.project.Project.domain.member;

import com.project.Project.domain.embedded.Coordinate;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecentMapLocation {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Coordinate coordinate;

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
