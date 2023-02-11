package com.project.Project.repository.review;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    // @Query("select case when count(c)> 0 then true else false end from Car c where lower(c.model) like lower(:model)")

    @Query("select case when count(r) > 0 then true else false end from ReviewImage r where r.uuid.uuid = :uuid") // 이 쿼리가 좀 이상한디??
    boolean existsByUuid(@Param(value = "uuid") String uuid);
    @Query("select r from ReviewImage r where r.uuid.uuid = :uuid")
    Optional<ReviewImage> findByUuid(@Param(value = "uuid") String uuid);
    List<ReviewImage> findByReview(Review review);

    @Query("select r from ReviewImage  r where r.review.building = :building")
    List<ReviewImage> findByBuilding(@Param(value = "building")Building building);
}
