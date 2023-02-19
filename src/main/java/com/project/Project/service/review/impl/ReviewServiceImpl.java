package com.project.Project.service.review.impl;

import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.review.ReviewException;
import com.project.Project.loader.review.ReviewLoader;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.review.ReviewCustomRepository;
import com.project.Project.repository.review.ReviewEventListener;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.building.BuildingService;
import com.project.Project.service.review.ReviewGenerator;
import com.project.Project.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {


    private final BuildingRepository buildingRepository;
    private final ReviewRepository reviewRepository;
    private final BuildingService buildingService;

    private final EntityManager entityManager;
    private final ReviewCustomRepository reviewCustomRepository;
    private final ReviewEventListener eventListener;
    private final ReviewLoader reviewLoader;

    public List<Review> getReviewListByBuildingId(Long buildingId, List<Double> cursorIds, Pageable pageable) {

        /*
            1. buildingId로 building 조회 -> room 리스트 조회
            2. room 리스트의 reviewList들을 연결하여 반환
         */
        // 컨트롤러에서 존재하는 빌딩으로 검증되고 넘어왔으므로 바로 get
        List<Review> reviewList = reviewCustomRepository.findReviewsByBuildingId(buildingId, cursorIds, pageable);
        reviewList = reviewLoader.loadAllScores(reviewList);
        return reviewList;
    }


    public Review getReviewById(Long reviewId) {
        return reviewCustomRepository.findById(reviewId).orElseThrow(() -> new ReviewException("id에 해당하는 review가 없습니다.", ErrorCode.REVIEW_NOT_FOUND));
    }

    @Transactional
    public Long deleteById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
        reviewLoader.loadAllRelations(review);
        reviewRepository.delete(review);
        return reviewId;
    }


    @Transactional
    public Review saveReview(ReviewRequestDto.ReviewCreateDto request, Member author, Building building) {
        /*
            1. address로 빌딩 조회
            2. 빌딩의 room 조회
            3. room을 toReview로 넘겨서 review 생성
            4. 저장 후 응답
         */
        Optional<Review> review = reviewRepository.findReviewByAuthorAndBuilding(author.getId(), building.getId());
        if (review.isPresent()) {
            throw new ReviewException(ErrorCode.REVIEW_DUPLICATED);
        } else {
            Review createdReview = ReviewGenerator.createReview(request, author, building);
            Review savedReview = reviewRepository.save(createdReview);
            eventListener.updateOthers(savedReview);
            return savedReview;
        }

    }

    @Transactional
    public Long save(Review review) {
        return reviewRepository.save(review).getId();
    }
}
