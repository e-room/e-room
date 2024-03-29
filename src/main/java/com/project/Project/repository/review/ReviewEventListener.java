package com.project.Project.repository.review;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.building.BuildingSummary;
import com.project.Project.domain.building.BuildingToReviewCategory;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewCategory;
import com.project.Project.domain.review.ReviewToReviewCategory;
import com.project.Project.repository.EventListener;
import com.project.Project.repository.building.BuildingSummaryRepository;
import com.project.Project.repository.building.BuildingToReviewCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewEventListener implements EventListener {

    private final ReviewRepository reviewRepository;
    private final BuildingSummaryRepository buildingSummaryRepository;
    private final BuildingToReviewCategoryRepository buildingToReviewCategoryRepository;
    private final ReviewToReviewCategoryRepository reviewToReviewCategoryRepository;
    private final ReviewCategoryRepository reviewCategoryRepository;


    public void listenToCreateReview(Review review) {
        Building building = review.getBuilding();
        this.updateBuildingInfo(building);
    }

    public void listenToDeleteReview(Review deletedReview) {
        Building building = deletedReview.getBuilding();
        this.updateBuildingInfo(building);
    }

    private void updateBuildingInfo(Building building) {
        Long buildingId = building.getId();

        //Building Summary
        BuildingSummary buildingSummary = building.getBuildingSummary();

        Double avgScore = this.reviewRepository.findReviewsWithBuildingAndLock(buildingId).stream()
                .map(Review::getReviewToReviewCategoryList).flatMap(Collection::stream)
                .filter(elem -> elem.getReviewCategory().getType().equals(ReviewCategoryEnum.RESIDENCESATISFACTION))
                .mapToDouble(ReviewToReviewCategory::getScore)
                .average().orElse(0.0);
        Integer reviewCnt = this.reviewRepository.findReviewsWithBuildingAndLock(buildingId).size();
        buildingSummary.updateBuildingSummary(avgScore, Long.valueOf(reviewCnt));
        this.buildingSummaryRepository.save(buildingSummary);

        //BuildingToReviewCategory
        List<ReviewToReviewCategory> reviewToReviewCategoryList = this.reviewToReviewCategoryRepository.findReviewToCategoriesWithReviewAndBuildingAndLock(buildingId);
        List<ReviewCategoryEnum> values = List.of(ReviewCategoryEnum.values());
        List<BuildingToReviewCategory> buildingToReviewCategoryList = this.buildingToReviewCategoryRepository.findBuildingToReviewCategoriesByBuilding_Id(buildingId);
        for (ReviewCategoryEnum value : values) {
            BuildingToReviewCategory target = buildingToReviewCategoryList.stream().filter(elem -> elem.getReviewCategory().getType().equals(value)).findFirst().orElseGet(
                    () -> {
                        ReviewCategory reviewCategory = this.reviewCategoryRepository.findByType(value).get();
                        BuildingToReviewCategory buildingToReviewCategory = BuildingToReviewCategory.builder()
                                .building(building)
                                .reviewCategory(reviewCategory)
                                .build();
                        buildingToReviewCategoryList.add(buildingToReviewCategory);
                        return buildingToReviewCategory;
                    }
            );
            Double avg = reviewToReviewCategoryList.stream().filter(elem -> elem.getReviewCategory().getType().equals(value))
                    .mapToDouble(elem -> elem.getScore())
                    .average().orElse(0.0);
            target.setAvgScore(avg);
        }
        this.buildingToReviewCategoryRepository.saveAll(buildingToReviewCategoryList);
    }


}
