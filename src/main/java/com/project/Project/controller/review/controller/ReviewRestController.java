package com.project.Project.controller.review.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.CursorDto;
import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.building.dto.BuildingOptionalDto;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import com.project.Project.serializer.review.ReviewSerializer;
import com.project.Project.service.BuildingService;
import com.project.Project.service.ReviewImageService;
import com.project.Project.service.ReviewService;
import com.project.Project.service.RoomService;
import com.project.Project.validator.ExistBuilding;
import com.project.Project.validator.ExistReview;
import com.project.Project.validator.ExistRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
public class ReviewRestController {

    private final ReviewService reviewService;
    private final BuildingService buildingService;
    private final RoomService roomService;
    private final ReviewImageService reviewImageService;

    /* todo
        @GetMapping("/building/room/review")
     */

    /**
     * 3.2 리뷰_상세페이지<br>
     * - 특정 건물에 대한 리뷰 리스트를 반환<br>
     * - 3.2 리뷰_상세페이지에서 <strong>전체</strong>버튼을 눌렀을 때 사용
     *
     * @param buildingId 건물의 id
     * @param cursorDto  cursorId : 조회해서 받았던 리스트 중에 가장 마지막 원소의 Id | size : 한 번에 받고자 하는 원소의 개수
     * @return 건물 id에 해당하는 리뷰 리스트
     */
    @GetMapping("/buildig/{buildingId}/room/review")
    public List<ReviewResponseDto.ReviewListResponse> getReviewListByBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId, @ModelAttribute CursorDto cursorDto) {
        List<Review> reviewList = reviewService.getReviewListByBuildingId(buildingId, cursorDto.getCursorId(), PageRequest.of(0, cursorDto.getSize()));
        List<ReviewResponseDto.ReviewListResponse> reviewListResponseList =
                reviewList.stream()
                        .map(Review::toReviewListResponse)
                        .collect(Collectors.toList());
        return reviewListResponseList;
    }

    /**
     * 3.2 리뷰_상세페이지<br>
     * - 특정 방에 대한 리뷰 리스트를 반환<br>
     * - 3.2 리뷰_상세페이지에서 <strong>방(ex.102호)</strong>버튼을 눌렀을 때 사용
     *
     * @param roomId    방의 id
     * @param cursorDto cursorId : 조회해서 받았던 리스트 중에 가장 마지막 원소의 Id | size : 한 번에 받고자 하는 원소의 개수
     * @return 방 id에 해당하는 리뷰 리스트
     */
    @GetMapping("/building/room/{roomId}/review")
    public List<ReviewResponseDto.ReviewListResponse> getReviewListByRoom(@PathVariable("roomId") @ExistRoom Long roomId, @ModelAttribute CursorDto cursorDto) {
        List<Review> reviewList = reviewService.getReviewListByRoomId(roomId, cursorDto.getCursorId(), PageRequest.of(0, cursorDto.getSize()));
        List<ReviewResponseDto.ReviewListResponse> reviewListResponseList =
                reviewList.stream()
                        .map(Review::toReviewListResponse)
                        .collect(Collectors.toList());
        return reviewListResponseList;
    }
    /* todo
        @GetMapping("/building/room/review/{reviewId}")
    */


    /* todo
       등록, 삭제 API 인가 처리
       등록: 로그인한 사용자만 리뷰 등록 가능
       삭제: 본인의 리뷰만 삭제 가능
     */

    /**
     * 7.1 ~ 7.5 리뷰쓰기<br>
     * - 리뷰등록 API<br>
     *
     * @param request 등록할 리뷰
     * @return 등록된 리뷰의 id, 등록일시, affected row의 개수
     */
    @PostMapping("/building/room/review") // multipart/form-data 형태로 받음
    public ReviewResponseDto.ReviewCreateResponse createReview(@ModelAttribute @Valid ReviewRequestDto.ReviewCreateDto request, @AuthUser Member loginMember) {
        /*
            1. address로 빌딩 조회
            2. 빌딩의 room 조회
            3. room을 toReview로 넘겨서 review 생성
            4. 저장 후 응답
         */


        Address address = AddressDto.toAddress(request.getAddress());
        String buildingName = request.getBuildingName();
        Boolean hasElevator = request.getAdvantageKeywordList().stream().anyMatch((keyword) -> keyword.equalsIgnoreCase(KeywordEnum.ELEVATOR.toString()));
        BuildingOptionalDto buildingOptionalDto = new BuildingOptionalDto(buildingName, hasElevator);

        Long savedReviewId = 0L;
        Building building = buildingService.findByAddress(address)
                .orElseGet(() -> buildingService.createBuilding(address, buildingOptionalDto)); // 빌딩이 없는 경우 생성

        Room room = roomService.findByBuildingAndLineNumberAndRoomNumber(building, request.getRoomNumber(), request.getLineNumber())
                .orElse(roomService.createRoom(building, request.getLineNumber(), request.getRoomNumber())); // 방이 없는 경우 생성해줌.

        Review review = ReviewSerializer.toReview(request, loginMember, room);
        savedReviewId = reviewService.save(review);


        return ReviewResponseDto.ReviewCreateResponse.builder()
                .reviewId(savedReviewId)
                .createdAt(LocalDateTime.now())
                .build();
    }
    /* todo
        @PutMapping("/building/room/review/{reviewId}")
     */

    /**
     * 3.2 리뷰_상세페이지<br>
     * - 리뷰삭제 API
     *
     * @param reviewId 삭제할 리뷰 id
     * @return 삭제된 리뷰의 id, 등록일시, affected row의 개수
     */
    @DeleteMapping("/building/room/review/{reviewId}")
    public ReviewResponseDto.ReviewDeleteResponse deleteReview(@PathVariable("reviewId") @ExistReview Long reviewId) {
        Long deletedReviewId = reviewService.deleteById(reviewId);
        return ReviewResponseDto.ReviewDeleteResponse.builder()
                .reviewId(deletedReviewId)
                .deletedAt(LocalDateTime.now())
                .affectedRowCnt(3) // 어캐앎 ?? todo : affected row 하드코딩 해결 및 review API 전부 테스트
                .build();
    }
}
