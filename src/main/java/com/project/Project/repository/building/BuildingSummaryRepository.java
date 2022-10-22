package com.project.Project.repository.building;

import com.project.Project.domain.building.BuildingSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingSummaryRepository extends JpaRepository<BuildingSummary, Long> {
}
