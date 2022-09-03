package com.project.Project.service.impl;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.BuildingRepository;
import com.project.Project.repository.ReviewRepository;
import com.project.Project.repository.RoomRepository;
import com.project.Project.service.ReviewService;
import lombok.RequiredArgsConstructor;
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

    public List<Review> getReviewListByBuildingId(Long buildingId) {
        /*
            1. buildingId로 building 조회 -> room 리스트 조회
            2. room 리스트의 reviewList들을 연결하여 반환
         */
        // 컨트롤러에서 존재하는 빌딩으로 검증되고 넘어왔으므로 바로 get
        Building building = buildingRepository.findById(buildingId).get();
        List<Room> roomList = roomRepository.findByBuilding(building);
        List<Review> reviewList = new ArrayList<>();
        for(Room room : roomList) {
            reviewList.addAll(room.getReviewList());
        }
        return reviewList;
    }

    public List<Review> getReviewListByRoomId(Long roomId) {
        /*
            roomId로 room 조회 -> review 리스트 조회
         */
        // 컨트롤러에서 존재하는 룸으로 검증되고 넘어왔으므로 바로 get
        Room room = roomRepository.findById(roomId).get();
        List<Review> reviewList = reviewRepository.findByRoom(room);
        return reviewList;
    }

    @Transactional
    public Long deleteById(Long reviewId) {
        reviewRepository.deleteById(reviewId);
        return reviewId;
    }

    @Transactional
    public Long save(Review review) {
        return reviewRepository.save(review).getId();
    }
}
