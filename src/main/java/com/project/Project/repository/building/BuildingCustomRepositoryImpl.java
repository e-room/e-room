package com.project.Project.repository.building;

import com.project.Project.domain.building.Building;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.util.component.QueryDslUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.project.Project.domain.building.QBuilding.building;
import static com.project.Project.domain.building.QBuildingSummary.buildingSummary;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class BuildingCustomRepositoryImpl implements BuildingCustomRepository {
    private final EntityManager em;
    private final JPAQueryFactory factory;
    private static final String ID = "id";
    private static final String REVIEW_CNT = "reviewCnt";
    private static final String AVG_SCORE = "avgScore";

    @Autowired
    public BuildingCustomRepositoryImpl(EntityManager em, JPAQueryFactory factory) {
        this.em = em;
        this.factory = factory;
    }

    @Override
    public List<OnlyBuildingIdAndCoord> getBuildingMarking() {
        return factory.select(Projections.constructor(OnlyBuildingIdAndCoord.class,
                        building.coordinate,
                        building.id
                )).
                from(building)
                .innerJoin(building.buildingSummary, buildingSummary)
                .where(buildingSummary.reviewCnt.gt(0))
                .fetch();
    }

    @Override
    public List<Building> searchBuildings(String params, List<Double> cursorIds, Pageable pageable) {
        return searchBuildingsQuery(cursorIds, pageable).andThen(customOrderBy(pageable)).apply(params)
                .limit(pageable.getPageSize() + 1L)
                .fetch();
    }

    @Override
    public Optional<Building> findFullBuildingById(Long id, Set<String> hints) {
        EntityGraph<Building> graph = em.createEntityGraph(Building.class);
        for (String attribute : hints) {
            graph.addAttributeNodes(attribute);
        }

        return Optional.ofNullable(factory.selectFrom(building)
                .where(building.id.eq(id))
                .setHint("javax.persistence.fetchgraph", graph)
                .fetchOne());
    }

    @Override
    public List<Building> findBuildingsByIdIn(List<Long> ids, List<Double> cursorIds, Pageable pageable) {
        return findBuildingInQuery(cursorIds, pageable).andThen(customOrderBy(pageable)).apply(ids)
                .limit(pageable.getPageSize() + 1L)
                .fetch();
    }

    public Function<List<Long>, JPAQuery<Building>> findBuildingInQuery(List<Double> cursorIds, Pageable pageable) {
        return ids -> factory.selectFrom(building)
                .where(building.id.in(ids).and(cursorId(pageable, cursorIds, 0)));
    }

    public Function<String, JPAQuery<Building>> searchBuildingsQuery(List<Double> cursorIds, Pageable pageable) {
        return params -> factory.selectFrom(building)
                .where(buildingSearchPredicate(params), cursorId(pageable, cursorIds, 0));
    }

    public Function<JPAQuery<Building>, JPAQuery<Building>> customOrderBy(Pageable pageable) {
        List<OrderSpecifier<?>> orders = getAllOrderSpecifiers(pageable);
        return query -> query.orderBy(orders.stream().toArray(OrderSpecifier[]::new));
    }

    private BooleanBuilder notDeletedCondition() {
        return new BooleanBuilder()
                .and(building.deleted.ne(true));
    }

    private BooleanBuilder buildingSearchPredicate(String params) {
        return new BooleanBuilder()
                .and(notDeletedCondition())
                .and(
                        Expressions.stringTemplate("concat_ws(' ',{0}, {1}, {2}, {3}, {4}, {5})", building.address.siDo, building.address.siGunGu, building.address.eupMyeon, building.address.roadName, building.address.buildingNumber, building.buildingName)
                                .like("%" + params + "%")
                );
    }


    public List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case ID:
                        OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(direction, building, ID);
                        orders.add(orderId);
                        break;
                    case REVIEW_CNT:
                        OrderSpecifier<?> orderReviewCnt = QueryDslUtil.getSortedColumn(direction, building.buildingSummary, REVIEW_CNT);
                        orders.add(orderReviewCnt);
                        break;
                    case AVG_SCORE:
                        OrderSpecifier<?> orderScore = QueryDslUtil.getSortedColumn(direction, building.buildingSummary, AVG_SCORE);
                        orders.add(orderScore);
                        break;
                    default:
                        break;
                }
            }
        } else {
            OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(Order.DESC, building, ID);
            orders.add(orderId);
        }

        return orders;
    }

    public BooleanExpression cursorId(Pageable pageable, List<Double> cursorIds, Integer index) {
        if (index == cursorIds.size()) return null;
        Sort.Order order = pageable.getSort().get().collect(Collectors.toList()).get(index);
        // id < 파라미터를 첫 페이지에선 사용하지 않기 위한 동적 쿼리
        if (cursorIds.get(index) == null) {
            return null; // // BooleanExpression 자리에 null 이 반환되면 조건문에서 자동으로 제거
        } else if (order.getProperty().equals(REVIEW_CNT)) {
            BooleanExpression sub1 = null;
            BooleanExpression sub2 = null;
            if (index == cursorIds.size() - 1) {

                return building.buildingSummary.reviewCnt.lt(cursorIds.get(index).intValue());
            } else {
                sub1 = building.buildingSummary.reviewCnt.loe(cursorIds.get(index).intValue());
                Integer next = index + 1;
                sub2 = cursorId(pageable, cursorIds, next);
            }
            return sub1.and(building.buildingSummary.reviewCnt.lt(cursorIds.get(index).intValue()).or(sub2));
        } else if (order.getProperty().equals(AVG_SCORE)) {
            BooleanExpression sub1 = null;
            BooleanExpression sub2 = null;
            if (index == cursorIds.size() - 1) {
                return building.buildingSummary.avgScore.lt(cursorIds.get(index));
            } else {
                sub1 = building.buildingSummary.avgScore.loe(cursorIds.get(index));
                Integer next = index + 1;
                sub2 = cursorId(pageable, cursorIds, next);
            }
            return sub1.and(building.buildingSummary.avgScore.lt(cursorIds.get(index)).or(sub2));
        } else
            return building.id.lt(cursorIds.get(index).intValue());   //최신순
    }
}
