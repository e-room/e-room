package com.project.Project.service.interaction;

import com.project.Project.domain.member.Member;

public interface ReviewLikeService {
    Long updateReviewLike(Long reviewId, Member member);
}
