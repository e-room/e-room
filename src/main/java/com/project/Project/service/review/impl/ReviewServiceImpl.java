package com.project.Project.service.review.impl;

import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.building.dto.BuildingOptionalDto;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.room.dto.RoomBaseDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.repository.room.RoomRepository;
import com.project.Project.serializer.review.ReviewSerializer;
import com.project.Project.service.building.BuildingService;
import com.project.Project.service.fileProcess.FileProcessServiceImpl;
import com.project.Project.service.review.ReviewService;
import com.project.Project.service.room.RoomService;
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
    private final FileProcessServiceImpl fileProcessServiceImpl;
    private final BuildingService buildingService;
    private final RoomService roomService;

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

    @Transactional
    public Long deleteById(Long reviewId) {
        List<ReviewImage> reviewImageList = reviewRepository.findById(reviewId).get().getReviewImageList();
        for (ReviewImage reviewImage : reviewImageList) {
            fileProcessServiceImpl.deleteImage(reviewImage.getUrl());
        }
        reviewRepository.deleteById(reviewId);
        return reviewId;
    }


    @Transactional
    public Review createReview(ReviewRequestDto.ReviewCreateDto request, Member author) {
        /*
            1. address로 빌딩 조회
            2. 빌딩의 room 조회
            3. room을 toReview로 넘겨서 review 생성
            4. 저장 후 응답
         */
        Address address = AddressDto.toAddress(request.getAddress());
        BuildingOptionalDto buildingOptionalDto = request.getBuildingOptionalDto();

        Building building = buildingService.createBuilding(address, buildingOptionalDto);// 빌딩이 없는 경우 생성

        RoomBaseDto roomBaseDto = request.getRoomBaseDto();
        Room room = roomService.createRoom(building, roomBaseDto.getRoomNumber(), roomBaseDto.getLineNumber());
        Review review = reviewRepository.findReviewByMemberAndRoom(author.getId(), room.getId())
                .orElseGet(() -> ReviewSerializer.toReview(request, author, room));
        return reviewRepository.save(review);
    }

    @Transactional
    public Long save(Review review) {
        return reviewRepository.save(review).getId();
    }
}
