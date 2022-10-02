package com.project.Project.repository.building;

import com.project.Project.domain.building.Building;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;

public interface BuildingCustomRepository {
    Function<String,JPAQuery<Building>> searchBuildingsQuery ();
}
