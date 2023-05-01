package com.project.Project.repository.review;

import com.project.Project.domain.review.ReviewRead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewReadRepository extends JpaRepository<ReviewRead, Long> {

    Integer countByMemberId(Long memberId);

    List<ReviewRead> findByMemberId(Long memberId);

    Optional<ReviewRead> findByMemberIdAndReviewId(Long memberId, Long reviewId);
}
