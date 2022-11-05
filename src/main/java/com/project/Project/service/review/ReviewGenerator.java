package com.project.Project.service.review;

import com.project.Project.aws.s3.ReviewImagePackageMetaMeta;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewScoreDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.embedded.AnonymousStatus;
import com.project.Project.domain.enums.DTypeEnum;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.*;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.review.ReviewCategoryRepository;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.service.fileProcess.ReviewImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewGenerator {

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

    public static Review createReview(ReviewRequestDto.ReviewCreateDto request, Member member, Room room) {

        //reviewToReviewCategoryList 생성
        List<ReviewToReviewCategory> reviewToReviewCategoryList = createReviewToReviewCategoryList(request);

        // ReviewSummary
        ReviewSummary reviewSummary = initialReviewSummary();

        // ReviewToReviewKeywordList
        List<ReviewToReviewKeyword> selectedReviewAdvantageKeywordList = createKeywordList(request, request.getAdvantageKeywordList(), DTypeEnum.ADVANTAGE);
        List<ReviewToReviewKeyword> selectedReviewDisadvantageKeywordList = createKeywordList(request, request.getDisadvantageKeywordList(), DTypeEnum.DISADVANTAGE);

        //Review Entity
        Review review = createReviewEntity(request, member, room, reviewSummary);

        mappingEntities(reviewToReviewCategoryList, reviewSummary, selectedReviewAdvantageKeywordList, selectedReviewDisadvantageKeywordList, review);

        // ReviewImageList 생성
        createAndMapReviewImage(request, room, review);

        return review;
    }

    private static void createAndMapReviewImage(ReviewRequestDto.ReviewCreateDto request, Room room, Review review) {
        List<MultipartFile> imageFileList = request.getReviewImageList();
        ReviewImagePackageMetaMeta reviewImagePackageMeta = ReviewImagePackageMetaMeta.builder()
                .buildingId(room.getBuilding().getId())
                .roomId(room.getId())
                .build();
        /*
        todo: asynchronously
         */
        for (MultipartFile multipartFile : imageFileList) {
            staticReviewImageProcess.uploadImageAndMapToReview(multipartFile, reviewImagePackageMeta, review);
        }
    }

    private static void mappingEntities(List<ReviewToReviewCategory> reviewToReviewCategoryList, ReviewSummary reviewSummary, List<ReviewToReviewKeyword> selectedReviewAdvantageKeywordList, List<ReviewToReviewKeyword> selectedReviewDisadvantageKeywordList, Review review) {
        //ReviewKeywordList에 Review 할당
        selectedReviewAdvantageKeywordList.forEach((selectedReviewKeyword) -> selectedReviewKeyword.setReview(review));
        selectedReviewDisadvantageKeywordList.forEach((selectedReviewDisadvantageKeyword) -> selectedReviewDisadvantageKeyword.setReview(review));

        // ReviewToReviewCategory 연관관계
        reviewToReviewCategoryList.forEach((reviewToReviewCategory -> reviewToReviewCategory.setReview(review)));

        // Review - ReviewSummary
        reviewSummary.setReview(review);
    }

    private static Review createReviewEntity(ReviewRequestDto.ReviewCreateDto request, Member member, Room room, ReviewSummary reviewSummary) {
        return Review.builder()
                .residenceStartYear(request.getReviewResidencePeriodDto().getResidenceStartYear())
                .residenceDuration(request.getReviewResidencePeriodDto().getResidenceDuration())
                .deposit(request.getReviewBaseDto().getDeposit())
                .monthlyRent(request.getReviewBaseDto().getMonthlyRent())
                .managementFee(request.getReviewBaseDto().getManagementFee())
                .netLeasableArea(request.getReviewBaseDto().getNetLeasableArea())
                .advantageDescription(request.getAdvantageDescription())
                .disadvantageDescription(request.getDisadvantageDescription())
                .anonymousStatus(AnonymousStatus.generateAnonymousStatus())
                .reviewImageList(new ArrayList<>())
                .likeMemberList(new ArrayList<>())
                .reviewToReviewCategoryList(new ArrayList<>())
                .reviewToReviewKeywordList(new ArrayList<>())
                .author(member)
                .room(room)
                .reviewSummary(reviewSummary)
                .build();
    }

    private static ReviewSummary initialReviewSummary() {
        return ReviewSummary.builder()
                .likeCnt(0)
                .build();
    }

    private static List<ReviewToReviewCategory> createReviewToReviewCategoryList(ReviewRequestDto.ReviewCreateDto request) {
        // ReviewToReviewCategoryList 생성
        ArrayList<ReviewCategory> allReviewCategory = (ArrayList) staticReviewCategoryRepository.findAll();
        ReviewScoreDto reviewScores = request.getReviewScoreDto();

        //parallel stream 써보기
        return Arrays.stream(reviewScores.getClass().getDeclaredFields()).filter(field -> ReviewCategoryEnum.contains(field.getName()))
                .map((field) -> {
                    field.setAccessible(true);
                    try {
                        BigDecimal score = (BigDecimal) field.get(reviewScores);
                        ArrayList<ReviewCategory> clonedAllReviewCategory = (ArrayList) allReviewCategory.clone();
                        ReviewCategory targetReviewCategory = clonedAllReviewCategory.stream().filter(reviewCategory -> reviewCategory.getType().equals(ReviewCategoryEnum.valueOf(field.getName().toUpperCase(Locale.ROOT)))).findFirst().orElseThrow(() -> new RuntimeException());
                        ReviewToReviewCategory temp = ReviewToReviewCategory
                                .builder()
                                .score(score.doubleValue())
                                .build();
                        temp.setReviewCategory(targetReviewCategory);
                        return temp;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    private static List<ReviewToReviewKeyword> createKeywordList(ReviewRequestDto.ReviewCreateDto request, List<String> keywordList, DTypeEnum type) {
        List<KeywordEnum> advantageKeywordEnumList = keywordList.stream().map(KeywordEnum::valueOf).collect(Collectors.toList());
        List<ReviewKeyword> allReviewKeyword1 = staticReviewKeywordRepository.findAll();
        return allReviewKeyword1.stream().filter((keyword) -> keyword.getDType().equals(type))
                .filter((advantageKeyword) -> advantageKeywordEnumList.contains(advantageKeyword.getKeywordType()))
                .map((reviewKeyword) -> {
                    ReviewToReviewKeyword temp =
                            ReviewToReviewKeyword.builder().build();
                    temp.setReviewKeyword(reviewKeyword);
                    return temp;
                }).collect(Collectors.toList());
    }
}
