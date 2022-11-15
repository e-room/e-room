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

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewEventListener implements EventListener {

    private final ReviewRepository reviewRepository;
    private final BuildingSummaryRepository buildingSummaryRepository;
    private final BuildingToReviewCategoryRepository buildingToReviewCategoryRepository;
    private final ReviewToReviewCategoryRepository reviewToReviewCategoryRepository;
    private final ReviewCategoryRepository reviewCategoryRepository;
    private final EntityManager entityManager;

    //    public ReviewEventListener(@Lazy ReviewRepository reviewRepository,
//                               @Lazy BuildingSummaryRepository buildingSummaryRepository,
//                               @Lazy BuildingToReviewCategoryRepository buildingToReviewCategoryRepository,
//                               @Lazy ReviewToReviewCategoryRepository reviewToReviewCategoryRepository,
//                               @Lazy ReviewCategoryRepository reviewCategoryRepository,
//                               @Lazy EntityManager entityManager) {
//        this.reviewRepository = reviewRepository;
//        this.buildingSummaryRepository = buildingSummaryRepository;
//        this.buildingToReviewCategoryRepository = buildingToReviewCategoryRepository;
//        this.reviewToReviewCategoryRepository = reviewToReviewCategoryRepository;
//        this.reviewCategoryRepository = reviewCategoryRepository;
//        this.entityManager = entityManager;
//    }
    //    @PostUpdate
//    @PostPersist
//    @PostRemove
//    @Transactional
    public void updateOthers(Review review) {
        System.out.println("BBBBBBB");
//        entityManager.flush();
        System.out.println("CCCCCCC");
        Building building = review.getRoom().getBuilding();
        Long buildingId = building.getId();

        //Building Summary
        BuildingSummary buildingSummary = building.getBuildingSummary();
        System.out.println("DDDDD");
//        System.out.println(entityManager.isJoinedToTransaction());
//        entityManager.clear();
        List<Review> reviewList = this.reviewRepository.findReviewsWithRoomAndBuildingAndLock(buildingId);
        System.out.println("EEEEE");

        Double avgScore = this.reviewRepository.findReviewsWithRoomAndBuildingAndLock(buildingId).stream()
                .map(Review::getReviewToReviewCategoryList).flatMap(inner -> inner.stream())
                .filter(elem -> elem.getReviewCategory().getType().equals(ReviewCategoryEnum.RESIDENCESATISFACTION))
                .mapToDouble(ReviewToReviewCategory::getScore)
                .average().orElse(0.0);
        Integer reviewCnt = this.reviewRepository.findReviewsWithRoomAndBuildingAndLock(buildingId).size();
        buildingSummary.updateBuildingSummary(avgScore, Long.valueOf(reviewCnt));
        this.buildingSummaryRepository.save(buildingSummary);

        //BuildingToReviewCategory
        List<ReviewToReviewCategory> reviewToReviewCategoryList = this.reviewToReviewCategoryRepository.findReviewToCategoriesWithReviewAndRoomAndBuildingAndLock(buildingId);
        List<Long> reviewIds = reviewList.stream().map(Review::getId).collect(Collectors.toList());
//        List<ReviewToReviewCategory> reviewToReviewCategoryList1 = this.reviewToReviewCategoryRepository.findReviewToReviewCategoriesByReviewIds(reviewIds);
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
            Double avg = reviewToReviewCategoryList.stream().filter((elem) -> elem.getReviewCategory().getType().equals(value))
                    .mapToDouble(elem -> elem.getScore())
                    .average().orElse(0.0);
            target.setAvgScore(avg);
        }
        this.buildingToReviewCategoryRepository.saveAll(buildingToReviewCategoryList);
    }
}
