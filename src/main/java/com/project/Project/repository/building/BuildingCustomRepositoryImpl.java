package com.project.Project.repository.building;

import com.project.Project.domain.building.Building;
import com.project.Project.util.component.QueryDslUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.project.Project.domain.building.QBuilding.building;
import static com.project.Project.domain.building.QBuildingSummary.buildingSummary;
import static com.project.Project.domain.building.QBuildingToReviewCategory.buildingToReviewCategory;
import static com.project.Project.domain.room.QRoom.room;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
@RequiredArgsConstructor
public class BuildingCustomRepositoryImpl implements BuildingCustomRepository {
    private final JPAQueryFactory factory;

    @Override
    public List<Building> searchBuildings(String params, Long cursorId, Pageable pageable) {
        List<Building> results = searchBuildingsQuery(cursorId, pageable).andThen(customOrderBy(pageable)).apply(params)
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return results;
    }

    @Override
    public List<Building> findBuildingsByIdIn(List<Long> ids, Long cursorId, Pageable pageable) {
        List<Building> results = findBuildingInQuery(cursorId, pageable).andThen(customOrderBy(pageable)).apply(ids)
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return results;
    }

    public Function<List<Long>, JPAQuery<Building>> findBuildingInQuery(Long cursorId, Pageable pageable) {
        return (ids) -> factory.selectFrom(building)
                .innerJoin(building.roomList, room)
                .innerJoin(building.buildingToReviewCategoryList, buildingToReviewCategory)
                .innerJoin(building.buildingSummary, buildingSummary)
                .fetchJoin()
                .where(building.id.in(ids).and(notDeletedCondition()).and(cursorId(pageable, cursorId)));
    }

    public Function<String, JPAQuery<Building>> searchBuildingsQuery(Long cursorId, Pageable pageable) {
        return (params) -> factory.selectFrom(building)
                .innerJoin(building.buildingToReviewCategoryList, buildingToReviewCategory)
                .innerJoin(building.buildingSummary, buildingSummary)
                .fetchJoin()
                .where(buildingSearchPredicate(params), cursorId(pageable, cursorId));
    }

    public Function<JPAQuery<Building>, JPAQuery<Building>> customOrderBy(Pageable pageable) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
        return (query) -> query.orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new));
    }

    private BooleanBuilder notDeletedCondition() {
        return new BooleanBuilder()
                .and(building.deleted.ne(true));
    }

    private BooleanBuilder buildingSearchPredicate(String params) {
        return new BooleanBuilder()
                .orAllOf(building.address.siDo.contains(params),
                        building.address.siGunGu.contains(params),
                        building.address.eupMyeon.contains(params),
                        building.address.roadName.contains(params),
                        building.address.buildingNumber.contains(params),
                        building.buildingName.contains(params))
                .and(notDeletedCondition());
    }


    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "id":
                        OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(direction, building, "id");
                        ORDERS.add(orderId);
                        break;
                    case "reviewCnt":
                        OrderSpecifier<?> orderReviewCnt = QueryDslUtil.getSortedColumn(direction, building.buildingSummary, "reviewCnt");
                        ORDERS.add(orderReviewCnt);
                    case "avgScore":
                        OrderSpecifier<?> orderScore = QueryDslUtil.getSortedColumn(direction, building.buildingSummary, "avgScore");
                        ORDERS.add(orderScore);
                    default:
                        break;
                }
            }
        } else {
            OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(Order.DESC, building, "id");
            ORDERS.add(orderId);
        }

        return ORDERS;
    }

    private BooleanExpression cursorId(Pageable pageable, Long cursorId) {
        Sort.Order order = pageable.getSort().get().collect(Collectors.toList()).get(0);
        // id < 파라미터를 첫 페이지에선 사용하지 않기 위한 동적 쿼리
        if (cursorId == null) {
            return null; // // BooleanExpression 자리에 null 이 반환되면 조건문에서 자동으로 제거
        } else if (order.getProperty().equals("reviewCnt"))
            return building.buildingSummary.reviewCnt.lt(cursorId);
        else if (order.getProperty().equals("avgScore"))
            return building.buildingSummary.avgScore.lt(cursorId);
        else
            return building.id.lt(cursorId);   //최신순
    }
}
