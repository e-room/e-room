package com.project.Project.repository.review;

import com.project.Project.domain.review.ReviewRead;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.Project.domain.review.QReview.review;
import static com.project.Project.domain.review.QReviewRead.reviewRead;

@Repository
@RequiredArgsConstructor
public class ReviewReadCustomRepositoryImpl implements ReviewReadCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public List<ReviewRead> findReviewsByBuildingId(Long memberId, Long buildingId) {
        return factory.selectFrom(reviewRead)
                .leftJoin(reviewRead.review)
                .where(reviewRead.review.building.id.eq(buildingId))
                .fetch();
    }
}
