package com.project.Project.repository;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.building.BuildingToReviewCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BuildingToReviewCategoryRepository extends JpaRepository<BuildingToReviewCategory, Long> {
    @Query("select distinct brc from BuildingToReviewCategory brc join fetch brc.reviewCategory rc where brc.building = :id ")
    List<BuildingToReviewCategory> findBuildingToReviewCategoriesByBuilding_Id(Long id);
}
