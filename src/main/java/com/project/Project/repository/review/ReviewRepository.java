package com.project.Project.repository.review;

import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRoom(Room room);

    // 초기 조회 메서드 만들기
    List<Review> findByIdInOrderByCreatedAtDesc(List<Long> ids, Pageable pageable);

    List<Review> findByIdInAndIdLessThanOrderByCreatedAtDesc(List<Long> ids, Long id, Pageable pageable);

}
