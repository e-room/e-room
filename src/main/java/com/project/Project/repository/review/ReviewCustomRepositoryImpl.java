package com.project.Project.repository.review;

import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewToReviewCategory;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

import static com.project.Project.domain.building.QBuilding.building;
import static com.project.Project.domain.building.QBuildingSummary.buildingSummary;
import static com.project.Project.domain.building.QBuildingToReviewCategory.buildingToReviewCategory;
import static com.project.Project.domain.review.QReview.review;
import static com.project.Project.domain.review.QReviewCategory.reviewCategory;
import static com.project.Project.domain.review.QReviewToReviewCategory.reviewToReviewCategory;
import static com.project.Project.domain.room.QRoom.room;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory factory;


    @Transactional
    @Override
    public long updateReview(Review updatedReview) {
        long affectedRow = factory.update(review)
                .set(review.reviewSummary, updatedReview.getReviewSummary())
                .set(review.reviewToReviewCategoryList, updatedReview.getReviewToReviewCategoryList())
                .set(review.anonymousStatus, updatedReview.getAnonymousStatus())
                .set(review.member, updatedReview.getMember())
                .set(review.room, updatedReview.getRoom())
                .set(review.advantageDescription, updatedReview.getAdvantageDescription())
                .set(review.disadvantageDescription, updatedReview.getDisadvantageDescription())
                .set(review.deposit, updatedReview.getDeposit())
                .set(review.residenceStartYear, updatedReview.getResidenceStartYear())
                .set(review.residenceDuration, updatedReview.getResidenceDuration())
                .set(review.likeMemberList, updatedReview.getLikeMemberList())
                .set(review.managementFee, updatedReview.getManagementFee())
                .set(review.monthlyRent, updatedReview.getMonthlyRent())
                .set(review.netLeasableArea, updatedReview.getNetLeasableArea())
                .where(review.id.eq(updatedReview.getId()))
                .execute();
        affectedRow += postUpdate(updatedReview);
        //affectedRow must be 1(review) + 1(buildingSummary) + ReviewCategoryEnum.size()
        return affectedRow;
    }

    //postUpdate for QueryDsl Update and delete
    // queryDsl doesn't support insert, it must be done through em or jpaRepository
    private long postUpdate(Review updatedReview) {
        Long buildingId = updatedReview.getRoom().getBuilding().getId();

        //building Summary
        Tuple buildingSummaryResults = factory.select(reviewToReviewCategory.score.avg(), review.countDistinct())
                .from(review)
                .innerJoin(review.reviewToReviewCategoryList, reviewToReviewCategory)
                .innerJoin(reviewToReviewCategory.reviewCategory, reviewCategory)
                .innerJoin(review.room, room)
                .innerJoin(room.building, building)
                .where(building.id.eq(buildingId), reviewCategory.type.eq(ReviewCategoryEnum.RESIDENCESATISFACTION))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).fetchOne();

        Double avgScore = buildingSummaryResults.get(reviewToReviewCategory.score.avg());
        Long reviewCnt = buildingSummaryResults.get(review.countDistinct());

        long affectedRow = factory.update(buildingSummary)
                .set(buildingSummary.avgScore, avgScore)
                .set(buildingSummary.reviewCnt, reviewCnt)
                .where(buildingSummary.building.id.eq(buildingId))
                .execute();

        //buildingToReviewCategory
        List<ReviewToReviewCategory> buildingToReviewCategoryResults = factory.selectFrom(reviewToReviewCategory)
                .innerJoin(reviewToReviewCategory.review, review)
                .innerJoin(review.room, room)
                .innerJoin(room.building, building)
                .where(building.id.eq(buildingId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).fetch();
        List<ReviewCategoryEnum> values = List.of(ReviewCategoryEnum.values());
        for (ReviewCategoryEnum value : values) {
            Double avg = buildingToReviewCategoryResults.stream().filter((elem) -> elem.getReviewCategory().getType().equals(value))
                    .mapToDouble(elem -> elem.getScore())
                    .average().orElse(0.0);
            affectedRow += factory.update(buildingToReviewCategory)
                    .set(buildingToReviewCategory.avgScore, avg)
                    .where(buildingToReviewCategory.building.id.eq(buildingId), buildingToReviewCategory.reviewCategory.type.eq(value))
                    .execute();
        }
        return affectedRow;
    }

    @Override
    public long softDeleteReview(Review targetReview) {
        LocalDateTime current = LocalDateTime.now();
        targetReview.setDeleted(true);
        targetReview.setUpdatedAt(current);
        long affectedRow = factory.update(review).set(review.deleted, true)
                .set(review.updatedAt, current)
                .where(review.id.eq(targetReview.getId()))
                .execute();
        affectedRow += postUpdate(targetReview);
        return affectedRow;
    }
}
