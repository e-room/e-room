package com.project.Project.repository.interaction;

import com.project.Project.domain.member.Member;
import com.project.Project.domain.interaction.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByMemberAndReview_Id(Member member, Long reviewId);
    void deleteByMember(Member member);
}
