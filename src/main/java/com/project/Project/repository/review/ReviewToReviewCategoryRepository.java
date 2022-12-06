package com.project.Project.repository.review;

import com.project.Project.domain.review.ReviewToReviewCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

public interface ReviewToReviewCategoryRepository extends JpaRepository<ReviewToReviewCategory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(value = "RTRC.withReviewAndRoomAndBuilding", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select rtrc from ReviewToReviewCategory rtrc where rtrc.review.room.building.id = :buildingId")
    List<ReviewToReviewCategory> findReviewToCategoriesWithReviewAndRoomAndBuildingAndLock(@Param("buildingId") Long buildingId);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select rtrc from ReviewToReviewCategory rtrc where rtrc.review.id in :reviewIds")
    List<ReviewToReviewCategory> findReviewToReviewCategoriesByReviewIds(@Param("reviewIds") List<Long> reviewIds);
}
