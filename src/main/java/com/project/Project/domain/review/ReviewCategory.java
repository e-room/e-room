package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.building.BuildingToReviewCategory;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.repository.review.ReviewCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SQLDelete(sql = "UPDATE review_category SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class ReviewCategory extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "reviewCategory", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ReviewToReviewCategory> reviewToReviewCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "reviewCategory", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<BuildingToReviewCategory> buildingToReviewCategoryList = new ArrayList<>();

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private ReviewCategoryEnum type;

    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }

    public static void init(ReviewCategoryRepository reviewCategoryRepository) {

        List<ReviewCategoryEnum> reviewCategoryEnums = List.of(ReviewCategoryEnum.values());
        List<ReviewCategoryEnum> reviewCategoryEnumListInDB = reviewCategoryRepository.findAll().stream().map(reviewCategory -> reviewCategory.getType()).collect(Collectors.toList());
        if (reviewCategoryRepository.findAll().size() == reviewCategoryEnums.size()) {
            return;
        } else if (reviewCategoryEnumListInDB.isEmpty()) {
            reviewCategoryEnums.stream().forEach((reviewCategoryEnum) -> {
                ReviewCategory reviewCategory = ReviewCategory.builder()
                        .type(reviewCategoryEnum)
                        .build();
                reviewCategoryRepository.save(reviewCategory);
            });
        } else {
            //something not in DB
            List<ReviewCategoryEnum> notInDbList = reviewCategoryEnums.stream().filter((categoryEnum) -> !(reviewCategoryEnumListInDB.contains(categoryEnum))).collect(Collectors.toList());
            //새롭게 저장
            notInDbList.stream().forEach(categoryEnum -> reviewCategoryRepository.save(
                    ReviewCategory.builder()
                            .type(categoryEnum)
                            .build()
            ));
        }
    }
}
