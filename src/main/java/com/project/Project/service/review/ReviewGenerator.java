package com.project.Project.service.review;

import com.project.Project.aws.s3.ReviewImagePackageMetaMeta;
import com.project.Project.aws.s3.ThumbnailImagePackageMetadata;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewScoreDto;

import com.project.Project.domain.member.Member;
import com.project.Project.domain.Thumbnail;
import com.project.Project.domain.Uuid;

import com.project.Project.domain.embedded.AnonymousStatus;
import com.project.Project.domain.enums.DTypeEnum;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.*;
import com.project.Project.domain.room.Room;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.etc.UuidException;
import com.project.Project.repository.ThumbnailRepository;
import com.project.Project.repository.review.ReviewCategoryRepository;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.repository.uuid.UuidRepository;
import com.project.Project.service.fileProcess.ReviewImageProcess;
import com.project.Project.service.fileProcess.ThumbnailImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewGenerator {

    private final WebClient nickNameWebClient;
    private final ReviewImageProcess reviewImageProcess;
    private final ReviewKeywordRepository reviewKeywordRepository;
    private final ReviewCategoryRepository reviewCategoryRepository;
    private final ThumbnailImageProcess thumbnailImageProcess;
    private final ThumbnailRepository thumbnailRepository;
    private final UuidRepository uuidRepository;

    private static WebClient staticNickNameWebClient;
    private static ReviewImageProcess staticReviewImageProcess;
    private static ReviewKeywordRepository staticReviewKeywordRepository;
    private static ReviewCategoryRepository staticReviewCategoryRepository;
    private static ThumbnailImageProcess staticThumbnailImageProcess;
    private static ThumbnailRepository staticThumbnailRepository;
    private static UuidRepository staticUuidRepository;

    @PostConstruct
    public void init() {
        staticReviewImageProcess = this.reviewImageProcess;
        staticReviewKeywordRepository = this.reviewKeywordRepository;
        staticReviewCategoryRepository = this.reviewCategoryRepository;
        staticNickNameWebClient = this.nickNameWebClient;
        staticThumbnailImageProcess = this.thumbnailImageProcess;
        staticThumbnailRepository = this.thumbnailRepository;
        staticUuidRepository = this.uuidRepository;
    }

    public static Review createReview(ReviewRequestDto.ReviewCreateDto request, Member member, Room room) {

        //reviewToReviewCategoryList 생성
        List<ReviewToReviewCategory> reviewToReviewCategoryList = createReviewToReviewCategoryList(request);

        // ReviewSummary
        ReviewSummary reviewSummary = initialReviewSummary();

        // ReviewToReviewKeywordList
        List<ReviewToReviewKeyword> selectedReviewAdvantageKeywordList = createKeywordList(request, request.getAdvantageKeywordList(), DTypeEnum.ADVANTAGE);
        List<ReviewToReviewKeyword> selectedReviewDisadvantageKeywordList = createKeywordList(request, request.getDisadvantageKeywordList(), DTypeEnum.DISADVANTAGE);

        //AnonymousStatus
        AnonymousStatus status = createAnonymousStatus(request.getReviewBaseDto().getIsAnonymous());

        //Review Entity
        Review review = createReviewEntity(request, member, room, reviewSummary, status);


        mappingEntities(reviewToReviewCategoryList, reviewSummary, selectedReviewAdvantageKeywordList, selectedReviewDisadvantageKeywordList, review);

        if (!request.getReviewImageList().isEmpty()) {
            // ReviewImageList 생성
            createAndMapReviewImage(request, room, review);
        }
        return review;
    }

    private static AnonymousStatus createAnonymousStatus(Boolean isAnonymous) {
        if (isAnonymous) {
            return generateAnonymousStatus();
        } else {
            return AnonymousStatus.builder()
                    .anonymousName(null)
                    .isAnonymous(Boolean.FALSE)
                    .build();
        }
    }

    private static void createAndMapReviewImage(ReviewRequestDto.ReviewCreateDto request, Room room, Review review) {
        List<MultipartFile> imageFileList = request.getReviewImageList();
        /*
        todo: asynchronously
         */
        List<ImageThumbnailMap> imageThumbnailMaps = new ArrayList<>();
        for (int i = 0; i < imageFileList.size(); i++) {
            imageThumbnailMaps.add(new ImageThumbnailMap(imageFileList.get(i), request.getThumbnailUuidIdList().get(i)));
        }
        imageThumbnailMaps.parallelStream().forEach((imageAndThumbnail) -> {
            Uuid uuid = staticUuidRepository.findById(imageAndThumbnail.getUuidId()).orElseThrow(() -> new UuidException(ErrorCode.UUID_NOT_FOUND));
            ThumbnailImagePackageMetadata thumbnailImagePackageMetadata = ThumbnailImagePackageMetadata.builder()
                    .createdAt(LocalDateTime.now())
                    .fileName(imageAndThumbnail.getImage().getOriginalFilename())
                    .uuid(uuid.getUuid())
                    .uuidEntity(uuid)
                    .build();
            Thumbnail thumbnail = staticThumbnailImageProcess.makeThumbnailAndUpload(imageAndThumbnail.getImage(), thumbnailImagePackageMetadata);
            staticThumbnailRepository.save(thumbnail);
            ReviewImagePackageMetaMeta reviewImagePackageMeta = ReviewImagePackageMetaMeta.builder()
                    .buildingId(room.getBuilding().getId())
                    .roomId(room.getId())
                    .uuid(thumbnail.getUuid().getUuid())
                    .uuidEntity(thumbnail.getUuid())
                    .build();
            staticReviewImageProcess.uploadImageAndMapToReview(imageAndThumbnail.getImage(), reviewImagePackageMeta, review);
        });

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

    private static Review createReviewEntity(ReviewRequestDto.ReviewCreateDto request, Member member, Room room, ReviewSummary reviewSummary, AnonymousStatus status) {
        return Review.builder()
                .residenceStartYear(request.getReviewResidencePeriodDto().getResidenceStartYear())
                .residenceDuration(request.getReviewResidencePeriodDto().getResidenceDuration())
                .deposit(request.getReviewBaseDto().getDeposit())
                .monthlyRent(request.getReviewBaseDto().getMonthlyRent())
                .managementFee(request.getReviewBaseDto().getManagementFee())
                .netLeasableArea(request.getReviewBaseDto().getNetLeasableArea())
                .advantageDescription(request.getAdvantageDescription())
                .disadvantageDescription(request.getDisadvantageDescription())
                .reviewImageList(new ArrayList<>())
                .likeMemberList(new ArrayList<>())
                .reviewToReviewCategoryList(new ArrayList<>())
                .reviewToReviewKeywordList(new ArrayList<>())
                .author(member) // todo : 이렇게 하면 Member쪽 reviewList에는 없지 않나??
                .room(room)
                .reviewSummary(reviewSummary)
                .anonymousStatus(status)
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
                        Double score = (Double) field.get(reviewScores);
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

    private static AnonymousStatus generateAnonymousStatus() {
        // todo : 형용사 + 명사의 이름 짓는 로직 포함
        String nickName = staticNickNameWebClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("format", "text").queryParam("max_length", 8).build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return AnonymousStatus.builder()
                .anonymousName(nickName)
                .isAnonymous(Boolean.TRUE)
                .build();
    }

    private static class ImageThumbnailMap {
        private MultipartFile image;
        private Long UuidId;

        public ImageThumbnailMap(MultipartFile image, Long UuidId) {
            this.image = image;
            this.UuidId = UuidId;
        }

        public MultipartFile getImage() {
            return image;
        }

        public Long getUuidId() {
            return UuidId;
        }
    }
}
