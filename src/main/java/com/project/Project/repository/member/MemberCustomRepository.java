package com.project.Project.repository.member;

import com.project.Project.domain.member.Member;

public interface MemberCustomRepository {

    public Long getReviewCnt(Member member);
}
