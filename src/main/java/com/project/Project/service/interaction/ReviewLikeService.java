package com.project.Project.service.interaction;

import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.member.Member;

public interface ReviewLikeService {
    ReviewLike updateReviewLike(Long reviewId, Member member);
}
