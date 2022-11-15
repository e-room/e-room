package com.project.Project.serializer.review;

import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewToReviewCategory;
import com.project.Project.repository.review.ReviewCategoryRepository;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.service.fileProcess.ReviewImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ReviewSerializer {
    private final ReviewImageProcess reviewImageProcess;
    private final ReviewKeywordRepository reviewKeywordRepository;
    private final ReviewCategoryRepository reviewCategoryRepository;

    private static ReviewImageProcess staticReviewImageProcess;
    private static ReviewKeywordRepository staticReviewKeywordRepository;
    private static ReviewCategoryRepository staticReviewCategoryRepository;

    @PostConstruct
    public void init() {
        this.staticReviewImageProcess = reviewImageProcess;
        this.staticReviewKeywordRepository = reviewKeywordRepository;
        this.staticReviewCategoryRepository = reviewCategoryRepository;
    }

    public static ReviewResponseDto.ReviewListResponse toReviewListResponse(Review review) {
        /*
        todo
        무조건 reviewCategory의 점수를 들고와야하는 입장에서 한 번 더 조회하는게 맞을까?
        객체 탐색이 나을지 NamedQuery가 나을지
        OSIV가 있으니까 들고올 때 한방 쿼리로 들고오고 객체 탐색하는 걸로
         */
        Double score = review.getReviewCategory(ReviewCategoryEnum.RESIDENCESATISFACTION)
                .map(ReviewToReviewCategory::getScore)
                .orElse(null);

        return ReviewResponseDto.ReviewListResponse.builder()
                .profilePictureUrl(review.getAuthor().getProfileImageUrl())
                .nickName(review.getAnonymousStatus().getAnonymousName())
                .roomId(review.getRoom().getId())
                .score(score)
                .residencePeriod(review.getResidenceDuration())
                .residenceDuration(review.getResidenceDuration())
                .netLeasableArea(review.getNetLeasableArea())
                .deposit(review.getDeposit())
                .monthlyRent(review.getMonthlyRent())
                .managementFee(review.getManagementFee())
                .advantage(review.getAdvantageKeywordEnumList())
                .advantageDescription(review.getAdvantageDescription())
                .disadvantage(review.getDisadvantageKeywordEnumList())
                .disadvantageDescription(review.getDisadvantageDescription())
                .build();
    }
}
