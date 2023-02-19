package com.project.Project.repository.member;

import com.project.Project.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.project.Project.domain.review.QReview.review;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory factory;

    @Override
    public Long getReviewCnt(Member member) {
        return factory.
                select(review.count())
                .from(review)
                .where(review.author.id.eq(member.getId()))
                .fetchFirst();
    }
}
