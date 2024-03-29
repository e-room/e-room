package com.project.Project.repository.interaction;

import com.project.Project.domain.enums.ReviewLikeStatus;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByMemberAndReview_Id(Member member, Long reviewId);

    List<ReviewLike> findByReviewLikeStatusAndMember(ReviewLikeStatus status, Member member);

    void deleteByMember(Member member);

    boolean existsByMember(Member member);

    void deleteAllByMember(Member member);
}
