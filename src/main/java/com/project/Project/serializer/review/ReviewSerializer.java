package com.project.Project.serializer.review;

import com.project.Project.aws.s3.ReviewImagePackageMetaMeta;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
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

    public static Review toReview(ReviewRequestDto.ReviewCreateDto request, Member member, Room room) {

//        Optional.ofNullable(staticReviewCategoryRepository).orElseThrow(()-> {
//            throw new RuntimeException();
//        });
        // ReviewToReviewCategoryList 생성
        ArrayList<ReviewCategory> allReviewCategory = (ArrayList) staticReviewCategoryRepository.findAll();

        List<ReviewToReviewCategory> reviewToReviewCategoryList = Arrays.stream(request.getClass().getDeclaredFields()).filter(field -> ReviewCategoryEnum.contains(field.getName()))
                .map((field) -> {
                    field.setAccessible(true);
                    try {
                        BigDecimal score = (BigDecimal) field.get(request);
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

        // ReviewSummary
        ReviewSummary reviewSummary = ReviewSummary.builder()
                .likeCnt(0)
                .build();

        // ReviewToReviewKeywordList
        List<String> advantageKeywordList = request.getAdvantageKeywordList();
        List<KeywordEnum> advantageKeywordEnumList = advantageKeywordList.stream().map(KeywordEnum::valueOf).collect(Collectors.toList());
        List<ReviewKeyword> allReviewKeyword1 = staticReviewKeywordRepository.findAll();
        List<ReviewToReviewKeyword> selectedReviewAdvantageKeywordList = allReviewKeyword1.stream().filter((keyword) -> keyword.getDType().equals(DTypeEnum.ADVANTAGE))
                .filter((advantageKeyword) -> advantageKeywordEnumList.contains(advantageKeyword.getKeywordType()))
                .map((reviewKeyword) -> {
                    ReviewToReviewKeyword temp =
                            ReviewToReviewKeyword.builder().build();
                    temp.setReviewKeyword(reviewKeyword);
                    return temp;
                }).collect(Collectors.toList());

        List<String> disadvantageKeywordList = request.getDisadvantageKeywordList();
        List<KeywordEnum> disadvantageKeywordEnumList = disadvantageKeywordList.stream().map(KeywordEnum::valueOf).collect(Collectors.toList());
        List<ReviewKeyword> allReviewKeyword2 = staticReviewKeywordRepository.findAll();
        List<ReviewToReviewKeyword> selectedReviewDisadvantageKeywordList = allReviewKeyword2.stream().filter((keyword) -> keyword.getDType().equals(DTypeEnum.DISADVANTAGE))
                .filter((disadvantageKeyword) -> disadvantageKeywordEnumList.contains(disadvantageKeyword.getKeywordType()))
                .map((reviewKeyword) -> {
                    ReviewToReviewKeyword temp =
                            ReviewToReviewKeyword.builder().build();
                    temp.setReviewKeyword(reviewKeyword);
                    return temp;
                }).collect(Collectors.toList());


        Review review = Review.builder()
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

        //ReviewKeywordList에 Review 할당
        selectedReviewAdvantageKeywordList.forEach((selectedReviewKeyword) -> selectedReviewKeyword.setReview(review));
        selectedReviewDisadvantageKeywordList.forEach((selectedReviewDisadvantageKeyword) -> selectedReviewDisadvantageKeyword.setReview(review));

        // ReviewToReviewCategory 연관관계
        reviewToReviewCategoryList.forEach((reviewToReviewCategory -> reviewToReviewCategory.setReview(review)));

        // Review - ReviewSummary
        reviewSummary.setReview(review);

        // ReviewImageList 생성
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

        return review;
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
