package com.project.Project.service;

import com.project.Project.domain.Member;

public interface FavoriteService {
    Long addFavoriteBuilding(Long buildingId, Member member);

    Long deleteFavoriteBuilding(Long buildingId, Member member);
}
