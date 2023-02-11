package com.project.Project.repository.review;

import com.project.Project.domain.review.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 초기 조회 메서드 만들기
    List<Review> findByIdInOrderByCreatedAtDesc(List<Long> ids, Pageable pageable);

    List<Review> findByIdInAndIdLessThanOrderByCreatedAtDesc(List<Long> ids, Long id, Pageable pageable);

    @EntityGraph(value = "Review.withRoomAndBuilding", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select review from Review review where review.building.id = :id")
    List<Review> findReviewsWithBuilding(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(value = "Review.withBuilding", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select review from Review review where review.building.id = :id")
    List<Review> findReviewsWithBuildingAndLock(@Param("id") Long buildingId);

    @Query("select review from Review review where review.author.id = :memberId and review.building.id = :buildingId")
    Optional<Review> findReviewByAuthorAndBuilding(@Param("memberId") Long memberId,  @Param("buildingId") Long buildingId);

}
