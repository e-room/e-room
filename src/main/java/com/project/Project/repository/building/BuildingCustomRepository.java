package com.project.Project.repository.building;

import com.project.Project.domain.building.Building;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public interface BuildingCustomRepository {
    Function<String, JPAQuery<Building>> searchBuildingsQuery(Long cursorId, Pageable pageable);

    List<Building> searchBuildings(String params, Long cursorId, Pageable pageable);

    List<Building> findBuildingsByIdIn(List<Long> ids, List<Long> cursorIds, Pageable pageable);

    Optional<Building> findFullBuildingById(Long id, Set<String> graph);
}
