package com.project.Project.service.interaction;

import com.project.Project.domain.Member;

public interface ReviewLikeService {
    Long updateReviewLike(Long reviewId, Member member);
}
