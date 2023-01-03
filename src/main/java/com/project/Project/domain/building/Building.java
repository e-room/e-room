package com.project.Project.domain.building;

import com.project.Project.controller.building.dto.BuildingOptionalDto;
import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.domain.room.Room;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE building SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UniqueAddress",
                        columnNames = {
                                "siDo",
                                "siGunGu",
                                "eupMyeon",
                                "roadName",
                                "buildingNumber"
                                // 상세주소와 참고항목은 같은 건물내에서도 다르므로 제외
                        }
                )
        }
)
public class Building extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    @Column(nullable = true)
    @ColumnDefault("''")
    @Builder.Default
    private String buildingName = "";

    @Embedded
    private Coordinate coordinate;

    private Boolean hasElevator;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Room> roomList = new ArrayList<>();

    @OneToMany(mappedBy = "building")
    @Builder.Default
    private List<Favorite> favoriteList = new ArrayList<>();


    @OneToMany(mappedBy = "building")
    @Builder.Default
    private List<BuildingToReviewCategory> buildingToReviewCategoryList = new ArrayList<>();

    @OneToOne(mappedBy = "building", cascade = CascadeType.ALL)
    private BuildingSummary buildingSummary;

    // TODO : 이미지 업로드 방법에 따라 추후 필드 추가. ex) S3업로드 or ec2 서버내에 업로드 등


    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }

    public void addRoom(Room room) {
        this.roomList.add(room);
        room.setBuilding(this);
    }

    public void addRooms(List<Room> rooms) {
        this.roomList.addAll(rooms);
        for (Room room : rooms) {
            room.setBuilding(this);
        }
    }

    public Building setOptions(BuildingOptionalDto dto) {
        if (dto.getBuildingName() != null && (this.buildingName == null || this.buildingName.equals(""))) {
            this.buildingName = dto.getBuildingName();
        }
        if (dto.getHasElevator() != null) {
            this.hasElevator = dto.getHasElevator();
        }
        return this;
    }

    // when we want to get ReviewCnt from Building, null-safe get reviewCnt or return null
    // NPE can be caused when BuildingSummary being null
    public static final Function<Building, Long> reviewCntOrZero = building -> Optional.ofNullable(building).map(Building::getBuildingSummary)
            .map(BuildingSummary::getReviewCnt)
            .orElse(0L);

    public static final Function<Building, Double> avgScoreOrNull = building -> Optional.ofNullable(building).map(Building::getBuildingSummary)
            .map(BuildingSummary::getAvgScore)
            .orElse(null);
}
