package com.project.Project.service.impl;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.RoomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final BuildingRepository buildingRepository;
    private final RoomRepository roomRepository;
    private final ReviewRepository reviewRepository;
    private final FileProcessService fileProcessService;

    public List<Review> getReviewListByBuildingId(Long buildingId, Long cursorId, Pageable page) {

        /*
            1. buildingId로 building 조회 -> room 리스트 조회
            2. room 리스트의 reviewList들을 연결하여 반환
         */
        // 컨트롤러에서 존재하는 빌딩으로 검증되고 넘어왔으므로 바로 get
        Building building = buildingRepository.findById(buildingId).get();
        List<Room> roomList = roomRepository.findByBuilding(building);

        List<Long> reviewIdList = new ArrayList<>();
        for (Room room : roomList) { // todo : id 추출 코드 개선하기
            for (Review review : room.getReviewList()) {
                reviewIdList.add(review.getId());
            }
        }

        return cursorId == null ?
                reviewRepository.findByIdInOrderByCreatedAtDesc(reviewIdList, page) :
                reviewRepository.findByIdInAndIdLessThanOrderByCreatedAtDesc(reviewIdList, cursorId, page);
    }

    public List<Review> getReviewListByRoomId(Long roomId, Long cursorId, Pageable page) {
        /*
            roomId로 room 조회 -> review 리스트 조회
         */
        // 컨트롤러에서 존재하는 룸으로 검증되고 넘어왔으므로 바로 get
        Room room = roomRepository.findById(roomId).get();

        List<Long> reviewIdList = new ArrayList<>();
        for (Review review : room.getReviewList()) {
            reviewIdList.add(review.getId());
        }

        return cursorId == null ?
                reviewRepository.findByIdInOrderByCreatedAtDesc(reviewIdList, page) :
                reviewRepository.findByIdInAndIdLessThanOrderByCreatedAtDesc(reviewIdList, cursorId, page);
    }

//    private List<Review> getReviews(Long cursorId, Pageable page) {
//        return cursorId == null ?
//                reviewRepository.findAllByOrderByCreatedAtDesc(page) :
//                reviewRepository.findByIdLessThanOrderByCreatedAtDesc(cursorId, page);
//
//    }

    @Transactional
    public Long deleteById(Long reviewId) {
        List<ReviewImage> reviewImageList = reviewRepository.findById(reviewId).get().getReviewImageList();
        for (ReviewImage reviewImage : reviewImageList) {
            fileProcessService.deleteImage(reviewImage.getUrl());
        }
        reviewRepository.deleteById(reviewId);
        return reviewId;
    }

    @Transactional
    public Long save(Review review) {
        return reviewRepository.save(review).getId();
    }
}
