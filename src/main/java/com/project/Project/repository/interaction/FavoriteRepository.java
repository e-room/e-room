package com.project.Project.repository.interaction;

import com.project.Project.domain.Member;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.interaction.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Favorite findByBuildingAndMember(Building building, Member member);

    boolean existsByMemberAndBuilding_Id(Member member, Long buildingId);
}
