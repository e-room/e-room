package com.project.Project.repository.review;

import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRoom(Room room);

    // 초기 조회 메서드 만들기
    List<Review> findByIdInOrderByCreatedAtDesc(List<Long> ids, Pageable pageable);

    List<Review> findByIdInAndIdLessThanOrderByCreatedAtDesc(List<Long> ids, Long id, Pageable pageable);

    @EntityGraph(value = "Review.withRoomAndBuilding", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select review from Review review where review.room.building.id = :id")
    List<Review> findReviewsWithRoomAndBuilding(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(value = "Review.withRoomAndBuilding", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select review from Review review where review.room.building.id = :id")
    List<Review> findReviewsWithRoomAndBuildingAndLock(Long id);

    Optional<Review> findReviewByMemberAndRoom(Long memberId, Long roomId);

}
