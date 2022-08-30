package com.project.Project.controller.review.controller;

import com.project.Project.Util.JsonResult;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import com.project.Project.service.ReviewService;
import com.project.Project.validator.ExistBuilding;
import com.project.Project.validator.ExistReview;
import com.project.Project.validator.ExistRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
public class ReviewRestController {

    private final ReviewService reviewService;


    /* todo
        @GetMapping("/building/room/review")
     */

    /**
     * 3.2 리뷰_상세페이지<br>
     * - 특정 건물에 대한 리뷰 리스트를 반환<br>
     * - 3.2 리뷰_상세페이지에서 <strong>전체</strong>버튼을 눌렀을 때 사용
     * @param buildingId 건물의 id
     * @return 건물 id에 해당하는 리뷰 리스트
     */
    @GetMapping("/buildig/{buildingId}/room/review")
    public List<ReviewResponseDto.ReviewListResponse> getReviewListByBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId){
        List<Review> reviewList = reviewService.getReviewListByBuildingId(buildingId);
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
     * @param roomId 방의 id
     * @return 방 id에 해당하는 리뷰 리스트
     */
    @GetMapping("/building/room/{roomId}/review")
    public List<ReviewResponseDto.ReviewListResponse> getReviewListByRoom(@PathVariable("roomId") @ExistRoom Long roomId){
        List<Review> reviewList = reviewService.getReviewListByRoomId(roomId);
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
     * @param request 등록할 리뷰
     * @return 등록된 리뷰의 id, 등록일시, affected row의 개수
     */
    @PostMapping("/building/room/review")
    public ReviewResponseDto.ReviewCreateResponse createReview(@RequestBody @Valid ReviewRequestDto.ReviewCreateDto request){
        /*
            1. address로 빌딩 조회
            2. 빌딩의 room 조회
            3. room을 toReview로 넘겨서 review 생성
            4. 저장 후 응답
         */
        Optional<Building> building = buildingService.findByAddress(request.getAddress());
        if(building.isPresent()) { // building이 존재할 때
            // room이 존재하는 경우 : 존재하는 room연관관계 맺은 review 저장

            // room이 존재하지 않는 경우 : room을 생성해준 후 review 저장
        } else { // building이 없을 때
            // building을 & room 생성 및 저장 -> review 저장
        }

        Review review = request.toReview();
        Long savedReviewId = reviewService.save(review);
        return ReviewResponseDto.ReviewCreateResponse.builder()
                .reviewId(savedReviewId)
                .createdAt(LocalDateTime.now())
                .affectedRowCnt(3) // todo: 어캐앎??
                .build();
    }
    /* todo
        @PutMapping("/building/room/review/{reviewId}")
     */

    /**
     * 3.2 리뷰_상세페이지<br>
     * - 리뷰삭제 API
     * @param reviewId 삭제할 리뷰 id
     * @return 삭제된 리뷰의 id, 등록일시, affected row의 개수
     */
    @DeleteMapping("/building/room/review/{reviewId}")
    public ReviewResponseDto.ReviewDeleteResponse deleteReview(@PathVariable("reviewId") @ExistReview Long reviewId){
        Long deletedReviewId = reviewService.deleteById(reviewId);
        return ReviewResponseDto.ReviewDeleteResponse.builder()
                .reviewId(deletedReviewId)
                .deletedAt(LocalDateTime.now())
                .affectedRowCnt(3) // 어캐앎 ??
                .build();
    }
}