package com.project.Project.repository.review;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    @Query("select r from ReviewImage r where r.review.room = :room")
    List<ReviewImage> findByRoom(@Param(value = "room") Room room);

    @Query("select r from ReviewImage  r where r.review.room.building = :building")
    List<ReviewImage> findByBuilding(@Param(value = "building")Building building);
}
