package com.project.Project.repository;

import com.project.Project.domain.building.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building,Long> {

}
