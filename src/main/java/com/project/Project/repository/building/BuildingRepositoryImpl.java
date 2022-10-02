package com.project.Project.repository.building;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.building.BuildingToReviewCategory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;

import static com.project.Project.domain.building.QBuilding.building;
import static com.project.Project.domain.building.QBuildingToReviewCategory.buildingToReviewCategory;

@Repository
@RequiredArgsConstructor
public class BuildingRepositoryImpl implements BuildingCustomRepository {
    private final JPAQueryFactory factory;


    public Function<String,JPAQuery<Building>> searchBuildingsQuery() {
        return (params) -> factory.selectFrom(building)
                .innerJoin(building.buildingToReviewCategoryList, buildingToReviewCategory).fetchJoin()
                .where(buildingSearchPredicate(params));
    }

    private BooleanBuilder notDeletedCondition() {
        return new BooleanBuilder()
                .and(building.deleted.ne(true));
    }

    private BooleanBuilder buildingSearchPredicate(String params) {
        return new BooleanBuilder()
                .orAllOf(building.address.metropolitanGovernment.contains(params),
                        building.address.basicLocalGovernment.contains(params),
                        building.address.siGunGu.contains(params),
                        building.address.eupMyeon.contains(params),
                        building.address.roadName.contains(params),
                        building.address.buildingNumber.contains(params),
                        building.buildingName.contains(params))
                .and(notDeletedCondition());
    }

    public Function<JPAQuery<Building>,JPAQuery<Building>> sortBy(Long cursorId, Pageable pageable) {

        return (query) -> query.orderBy(building.id.desc())
                .offset(cursorId)
                .limit(pageable.getPageSize());
    }
}
