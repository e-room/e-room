package com.project.Project.service;

import com.project.Project.domain.Member;
import com.project.Project.domain.building.Building;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteService {
    Long addFavoriteBuilding(Long buildingId, Member member);

    Long deleteFavoriteBuilding(Long buildingId, Member member);

    List<Building> getBuildingListByMember(Member member, List<Double> cursorIds,  Pageable pageable);
}
