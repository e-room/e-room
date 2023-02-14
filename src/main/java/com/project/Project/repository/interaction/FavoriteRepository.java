package com.project.Project.repository.interaction;

import com.project.Project.domain.interaction.Favorite;
import com.project.Project.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.Project.domain.building.Building;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    void deleteByMember(Member member);

    Favorite findByBuildingAndMember(Building building, Member member);

    boolean existsByMemberAndBuilding_Id(Member member, Long buildingId);

    List<Favorite> findByMember(Member member);

    boolean existsByMember(Member member);

    void deleteAllByMember(Member member);
}
