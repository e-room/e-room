package com.project.Project.repository.review;

import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewToReviewCategory;
import com.project.Project.util.component.QueryDslUtil;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.project.Project.domain.building.QBuilding.building;
import static com.project.Project.domain.building.QBuildingSummary.buildingSummary;
import static com.project.Project.domain.building.QBuildingToReviewCategory.buildingToReviewCategory;
import static com.project.Project.domain.review.QReview.review;
import static com.project.Project.domain.review.QReviewCategory.reviewCategory;
import static com.project.Project.domain.review.QReviewToReviewCategory.reviewToReviewCategory;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public Function<Long, JPAQuery<Review>> findReviewQueryByBuildingId(List<Double> cursorIds, Pageable pageable) {
        return (buildingId) -> factory.selectFrom(review)
                .innerJoin(review.reviewToReviewCategoryList, reviewToReviewCategory)
                .innerJoin(reviewToReviewCategory.reviewCategory, reviewCategory)
                .on(reviewCategory.type.eq(ReviewCategoryEnum.RESIDENCESATISFACTION))
                .where(cursorId(pageable, cursorIds, 0), review.building.id.eq(buildingId));
    }

    @Override
    public Optional<Review> findById(Long reviewId) {
        Review result = factory.selectFrom(review)
                .where(review.id.eq(reviewId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    //    public Function<Long, JPAQuery<Review>> findReviewQueryByRoomId(List<Double> cursorIds, Pageable pageable) {
//        return (roomId) -> factory.selectFrom(review)
//                .innerJoin(review.reviewToReviewCategoryList, reviewToReviewCategory)
//                .innerJoin(reviewToReviewCategory.reviewCategory, reviewCategory)
//                .on(reviewCategory.type.eq(ReviewCategoryEnum.RESIDENCESATISFACTION))
//                .where(cursorId(pageable, cursorIds, 0), review.room.id.eq(roomId));
//    }

    public List<Review> findReviewsByBuildingId(Long buildingId, List<Double> cursorIds, Pageable pageable) {
        List<Review> reviewList = findReviewQueryByBuildingId(cursorIds, pageable).andThen(customOrderBy(pageable)).apply(buildingId).limit(pageable.getPageSize() + 1).fetch();
        return reviewList;
    }


//    @Override
//    public List<Review> findReviewsByRoomId(Long roomId, List<Double> cursorIds, Pageable pageable) {
//        List<Review> reviewList = findReviewQueryByRoomId(cursorIds, pageable).andThen(customOrderBy(pageable)).apply(roomId).limit(pageable.getPageSize() + 1).fetch();
//        return reviewList;
//    }

    public Function<JPAQuery<Review>, JPAQuery<Review>> customOrderBy(Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
        return (query) -> query.orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new));
    }


    public List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "id":
                        OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(direction, review, "id");
                        ORDERS.add(orderId);
                        break;
                    case "likeCnt":
                        OrderSpecifier<?> orderLikeCnt = QueryDslUtil.getSortedColumn(direction, review.reviewSummary, "likeCnt");
                        ORDERS.add(orderLikeCnt);
                        break;
                    case "score":
                        OrderSpecifier<?> orderReviewCnt = QueryDslUtil.getSortedColumn(direction, review.reviewToReviewCategoryList, "score");
                        ORDERS.add(orderReviewCnt);
                }
            }
        } else {
            OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(Order.DESC, building, "id");
            ORDERS.add(orderId);
        }

        return ORDERS;
    }

    public BooleanExpression cursorId(Pageable pageable, List<Double> cursorIds, Integer index) {
        if (index == cursorIds.size()) return null;
        Sort.Order order = pageable.getSort().get().collect(Collectors.toList()).get(index);
        // id < 파라미터를 첫 페이지에선 사용하지 않기 위한 동적 쿼리
        if (cursorIds.get(index) == null) {
            return null; // // BooleanExpression 자리에 null 이 반환되면 조건문에서 자동으로 제거
        } else if (order.getProperty().equals("likeCnt")) {
            BooleanExpression sub1 = null;
            BooleanExpression sub2 = null;
            if (index == cursorIds.size() - 1) {

                return review.reviewSummary.likeCnt.lt(cursorIds.get(index).intValue());
            } else {
                sub1 = review.reviewSummary.likeCnt.loe(cursorIds.get(index).intValue());
                Integer next = index + 1;
                sub2 = cursorId(pageable, cursorIds, next);
            }
            return sub1.and(review.reviewSummary.likeCnt.lt(cursorIds.get(index).intValue()).or(sub2));
        } else if (order.getProperty().equals("score")) {
            BooleanExpression sub1 = null;
            BooleanExpression sub2 = null;
            if (index == cursorIds.size() - 1) {
                return reviewToReviewCategory.score.lt(cursorIds.get(index));
            } else {
                sub1 = reviewToReviewCategory.score.loe(cursorIds.get(index));
                Integer next = index + 1;
                sub2 = cursorId(pageable, cursorIds, next);
            }
            return sub1.and(reviewToReviewCategory.score.lt(cursorIds.get(index)).or(sub2));
        } else return review.id.lt(cursorIds.get(index).intValue());   //최신순
    }

    @Transactional
    @Override
    public long updateReview(Review updatedReview) {
        long affectedRow = factory.update(review).set(review.reviewSummary, updatedReview.getReviewSummary()).set(review.reviewToReviewCategoryList, updatedReview.getReviewToReviewCategoryList()).set(review.anonymousStatus, updatedReview.getAnonymousStatus()).set(review.author, updatedReview.getAuthor()).set(review.advantageDescription, updatedReview.getAdvantageDescription()).set(review.disadvantageDescription, updatedReview.getDisadvantageDescription()).set(review.deposit, updatedReview.getDeposit()).set(review.residenceStartYear, updatedReview.getResidenceStartYear()).set(review.residenceDuration, updatedReview.getResidenceDuration()).set(review.likeMemberList, updatedReview.getLikeMemberList()).set(review.managementFee, updatedReview.getManagementFee()).set(review.monthlyRent, updatedReview.getMonthlyRent()).set(review.netLeasableArea, updatedReview.getNetLeasableArea()).where(review.id.eq(updatedReview.getId())).execute();
        affectedRow += postUpdate(updatedReview);
        //affectedRow must be 1(review) + 1(buildingSummary) + ReviewCategoryEnum.size()
        return affectedRow;
    }

    //postUpdate for QueryDsl Update and delete
    // queryDsl doesn't support insert, it must be done through em or jpaRepository
    private long postUpdate(Review updatedReview) {
        Long buildingId = updatedReview.getBuilding().getId();

        //building Summary
        Tuple buildingSummaryResults = factory.select(reviewToReviewCategory.score.avg(), review.countDistinct()).from(review).innerJoin(review.reviewToReviewCategoryList, reviewToReviewCategory).innerJoin(reviewToReviewCategory.reviewCategory, reviewCategory).innerJoin(review.building, building).where(building.id.eq(buildingId), reviewCategory.type.eq(ReviewCategoryEnum.RESIDENCESATISFACTION)).setLockMode(LockModeType.PESSIMISTIC_WRITE).fetchOne();

        Double avgScore = buildingSummaryResults.get(reviewToReviewCategory.score.avg());
        Long reviewCnt = buildingSummaryResults.get(review.countDistinct());

        long affectedRow = factory.update(buildingSummary).set(buildingSummary.avgScore, avgScore).set(buildingSummary.reviewCnt, reviewCnt).where(buildingSummary.building.id.eq(buildingId)).execute();

        //buildingToReviewCategory
        List<ReviewToReviewCategory> buildingToReviewCategoryResults = factory.selectFrom(reviewToReviewCategory).innerJoin(reviewToReviewCategory.review, review).innerJoin(review.building, building).where(building.id.eq(buildingId)).setLockMode(LockModeType.PESSIMISTIC_WRITE).fetch();
        List<ReviewCategoryEnum> values = List.of(ReviewCategoryEnum.values());
        for (ReviewCategoryEnum value : values) {
            Double avg = buildingToReviewCategoryResults.stream().filter((elem) -> elem.getReviewCategory().getType().equals(value)).mapToDouble(elem -> elem.getScore()).average().orElse(0.0);
            affectedRow += factory.update(buildingToReviewCategory).set(buildingToReviewCategory.avgScore, avg).where(buildingToReviewCategory.building.id.eq(buildingId), buildingToReviewCategory.reviewCategory.type.eq(value)).execute();
        }
        return affectedRow;
    }

    @Override
    public long softDeleteReview(Review targetReview) {
        LocalDateTime current = LocalDateTime.now();
        targetReview.setDeleted(true);
        targetReview.setUpdatedAt(current);
        long affectedRow = factory.update(review).set(review.deleted, true).set(review.updatedAt, current).where(review.id.eq(targetReview.getId())).execute();
        affectedRow += postUpdate(targetReview);
        return affectedRow;
    }
}
