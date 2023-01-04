package com.project.Project.repository.review;

import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    @Query("select count(r.uuid) > 0 from ReviewImage r")
    boolean existsByUuid(@Param(value = "uuid") String uuid);
    @Query("select r from ReviewImage r where r.uuid.uuid = :uuid")
    Optional<ReviewImage> findByUuid(@Param(value = "uuid") String uuid);
    List<ReviewImage> findByReview(Review review);
}
