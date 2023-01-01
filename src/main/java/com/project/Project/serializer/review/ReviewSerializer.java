package com.project.Project.serializer.review;

import com.project.Project.auth.dto.MemberDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.enums.DTypeEnum;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewKeyword;
import com.project.Project.domain.review.ReviewToReviewCategory;
import com.project.Project.domain.review.ReviewToReviewKeyword;
import com.project.Project.domain.room.Room;
import com.project.Project.loader.review.ReviewLoader;
import com.project.Project.repository.review.ReviewCategoryRepository;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.serializer.member.MemberSerializer;
import com.project.Project.serializer.room.RoomSerializer;
import com.project.Project.service.fileProcess.ReviewImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewSerializer {
    private final ReviewImageProcess reviewImageProcess;
    private final ReviewKeywordRepository reviewKeywordRepository;
    private final ReviewCategoryRepository reviewCategoryRepository;
    private final ReviewLoader reviewLoader;

    private static ReviewImageProcess staticReviewImageProcess;
    private static ReviewKeywordRepository staticReviewKeywordRepository;
    private static ReviewCategoryRepository staticReviewCategoryRepository;
    private static ReviewLoader staticReviewLoader;


    @PostConstruct
    public void init() {
        staticReviewImageProcess = this.reviewImageProcess;
        staticReviewKeywordRepository = this.reviewKeywordRepository;
        staticReviewCategoryRepository = this.reviewCategoryRepository;
        staticReviewLoader = this.reviewLoader;
    }

    public static ReviewResponseDto.BaseReviewDto toBaseReviewDto(Review review) {
        review.getReviewToReviewKeywordList().stream().forEach((reviewToReviewKeyword) -> reviewToReviewKeyword.getReviewKeyword().getDType());
        Double score = review.getReviewCategory(ReviewCategoryEnum.RESIDENCESATISFACTION).map(ReviewToReviewCategory::getScore).orElse(null);

        List<KeywordEnum> advantageList = review.getReviewToReviewKeywordList().stream().filter(reviewToReviewKeyword -> reviewToReviewKeyword.getReviewKeyword().getDType().equals(DTypeEnum.ADVANTAGE)).map(ReviewToReviewKeyword::getReviewKeyword).map(ReviewKeyword::getKeywordType).collect(Collectors.toList());
        List<KeywordEnum> disadvantageList = review.getReviewToReviewKeywordList().stream().filter(reviewToReviewKeyword -> reviewToReviewKeyword.getReviewKeyword().getDType().equals(DTypeEnum.DISADVANTAGE)).map(ReviewToReviewKeyword::getReviewKeyword).map(ReviewKeyword::getKeywordType).collect(Collectors.toList());

        return ReviewResponseDto.BaseReviewDto.builder()
                .reviewId(review.getId())
                .score(score)
                .residencePeriod(review.getResidenceDuration())
                .residenceDuration(review.getResidenceDuration())
                .netLeasableArea(review.getNetLeasableArea())
                .deposit(review.getDeposit())
                .monthlyRent(review.getMonthlyRent())
                .managementFee(review.getManagementFee())
                .advantage(advantageList)
                .advantageDescription(review.getAdvantageDescription())
                .disadvantage(disadvantageList)
                .disadvantageDescription(review.getDisadvantageDescription())
                .reviewLikeCnt(review.getReviewSummary().getLikeCnt())
                .build();
    }

    public static ReviewResponseDto.ReviewScoreDto toReviewScoreDto(Review review) {
        List<ReviewToReviewCategory> reviewToReviewCategoryList = review.getReviewToReviewCategoryList();
        if (reviewToReviewCategoryList.isEmpty() || reviewToReviewCategoryList == null) {
            review = staticReviewLoader.loadAllScores(review);
            reviewToReviewCategoryList = review.getReviewToReviewCategoryList();
        }
        ReviewResponseDto.ReviewScoreDto reviewScoreDto = new ReviewResponseDto.ReviewScoreDto();
        reviewToReviewCategoryList.stream().filter((rtrc) -> ReviewCategoryEnum.contains(rtrc.getReviewCategory().getType()))
                .forEach((rtrc) -> {
                    List<Field> fieldList = Arrays.asList(reviewScoreDto.getClass().getDeclaredFields());
                    Field targetField = fieldList.stream().filter((field) -> field.getName().equalsIgnoreCase(rtrc.getReviewCategory().getType().name())).findFirst().orElseThrow(() -> new RuntimeException());
                    targetField.setAccessible(true);
                    try {
                        targetField.set(reviewScoreDto, rtrc.getScore());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        return reviewScoreDto;
    }

    public static ReviewResponseDto.ReviewListDto toReviewListDto(Review review) {
        /*
        todo
        무조건 reviewCategory의 점수를 들고와야하는 입장에서 한 번 더 조회하는게 맞을까?
        객체 탐색이 나을지 NamedQuery가 나을지
        OSIV가 있으니까 들고올 때 한방 쿼리로 들고오고 객체 탐색하는 걸로
         */

        Room room = review.getRoom();
        MemberDto authorDto = null;
        if (review.getAnonymousStatus().getIsAnonymous()) {
            authorDto = MemberDto.builder().name(review.getAnonymousStatus().getAnonymousName()).email(null).picture(null).build();
        } else {
            authorDto = MemberSerializer.toDto(review.getAuthor());
        }

        return ReviewResponseDto.ReviewListDto.builder().baseRoomDto(RoomSerializer.toBaseRoomResponse(room))
                .baseReviewDto(toBaseReviewDto(review))
                .reviewScoreDto(toReviewScoreDto(review))
                .authorDto(authorDto).build();
    }

    public static ReviewResponseDto.ReviewCreateDto toReviewCreateDto(Long createdReviewId) {
        return ReviewResponseDto.ReviewCreateDto.builder()
                .reviewId(createdReviewId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ReviewResponseDto.ReviewDeleteDto toReviewDeleteDto(Long deletedReviewId) {
        return ReviewResponseDto.ReviewDeleteDto.builder()
                .reviewId(deletedReviewId)
                .deletedAt(LocalDateTime.now())
                .build();
    }
}
