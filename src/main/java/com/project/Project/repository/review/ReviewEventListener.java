package com.project.Project.repository.review;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.building.BuildingSummary;
import com.project.Project.domain.building.BuildingToReviewCategory;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewToReviewCategory;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.repository.EventListener;
import com.project.Project.repository.building.BuildingSummaryRepository;
import com.project.Project.repository.building.BuildingToReviewCategoryRepository;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.util.List;

@Component
public class ReviewEventListener implements EventListener {

    private static ReviewRepository reviewRepository;
    private static BuildingSummaryRepository buildingSummaryRepository;
    private static BuildingToReviewCategoryRepository buildingToReviewCategoryRepository;
    private static ReviewToReviewCategoryRepository reviewToReviewCategoryRepository;

    @PostUpdate
    @PostPersist
    @PostRemove
    public void updateOthers(Review review) {
        Building building = review.getRoom().getBuilding();
        Long buildingId = building.getId();

        //Building Summary
        BuildingSummary buildingSummary = building.getBuildingSummary();
        Double avgScore = reviewRepository.findReviewsWithRoomAndBuildingAndLock(buildingId).stream()
                .map(Review::getReviewToReviewCategoryList).flatMap(inner -> inner.stream())
                .filter(elem -> elem.getReviewCategory().getType().equals(ReviewCategoryEnum.RESIDENCESATISFACTION))
                .mapToDouble(ReviewToReviewCategory::getScore)
                .average().orElse(0.0);
        Integer reviewCnt = reviewRepository.findReviewsWithRoomAndBuildingAndLock(buildingId).size();
        buildingSummary.updateBuildingSummary(avgScore, Long.valueOf(reviewCnt));
        buildingSummaryRepository.save(buildingSummary);

        //BuildingToReviewCategory
        List<ReviewToReviewCategory> reviewToReviewCategoryList = reviewToReviewCategoryRepository.findReviewToCategoriesWithReviewAndRoomAndBuildingAndLock(buildingId);
        List<ReviewCategoryEnum> values = List.of(ReviewCategoryEnum.values());
        List<BuildingToReviewCategory> buildingToReviewCategoryList = buildingToReviewCategoryRepository.findBuildingToReviewCategoriesByBuilding_Id(buildingId);
        for (ReviewCategoryEnum value : values) {
            BuildingToReviewCategory target = buildingToReviewCategoryList.stream().filter(elem -> elem.getReviewCategory().getType().equals(value)).findFirst().orElseThrow(() -> new BuildingException(ErrorCode.BUILDING_NPE));
            Double avg = reviewToReviewCategoryList.stream().filter((elem) -> elem.getReviewCategory().getType().equals(value))
                    .mapToDouble(elem -> elem.getScore())
                    .average().orElse(0.0);
            target.setAvgScore(avg);
        }
        buildingToReviewCategoryRepository.saveAll(buildingToReviewCategoryList);
    }
}
