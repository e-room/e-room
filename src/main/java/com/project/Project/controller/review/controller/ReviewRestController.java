package com.project.Project.controller.review.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.auth.dto.MemberDto;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.serializer.review.ReviewSerializer;
import com.project.Project.service.building.BuildingService;
import com.project.Project.service.review.ReviewImageService;
import com.project.Project.service.review.ReviewService;
import com.project.Project.service.room.RoomService;
import com.project.Project.util.component.QueryDslUtil;
import com.project.Project.validator.ExistBuilding;
import com.project.Project.validator.ExistReview;
import com.project.Project.validator.ExistRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * @param cursorIds  : 조회해서 받았던 리스트 중에 가장 마지막 원소를 식별하는 cursor| size : 한 번에 받고자 하는 원소의 개수
     * @return 건물 id에 해당하는 리뷰 리스트
     */
    @GetMapping("/building/{buildingId}/room/review")
    public ResponseEntity<Slice<ReviewResponseDto.ReviewListDto>> getReviewListByBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId,
                                                                                          @RequestParam(required = false) List<Double> cursorIds,
                                                                                          @PageableDefault(size = 10, sort = {"id"}, page = 0, direction = Sort.Direction.DESC) Pageable pageable) {
        if (cursorIds == null) cursorIds = new ArrayList<>();
        List<Review> reviewList = reviewService.getReviewListByBuildingId(buildingId, cursorIds, pageable);
        List<ReviewResponseDto.ReviewListDto> reviewListResponseList =
                reviewList.stream()
                        .map(ReviewSerializer::toReviewListDto)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(QueryDslUtil.toSlice(reviewListResponseList, pageable));
    }

    /**
     * 3.2 리뷰_상세페이지<br>
     * - 특정 방에 대한 리뷰 리스트를 반환<br>
     * - 3.2 리뷰_상세페이지에서 <strong>방(ex.102호)</strong>버튼을 눌렀을 때 사용
     *
     * @param roomId    방의 id
     * @param cursorIds : 조회해서 받았던 리스트 중에 가장 마지막 원소의 Id | size : 한 번에 받고자 하는 원소의 개수
     * @return 방 id에 해당하는 리뷰 리스트
     */
    @GetMapping("/building/room/{roomId}/review")
    public ResponseEntity<Slice<ReviewResponseDto.ReviewListDto>> getReviewListByRoom(@PathVariable("roomId") @ExistRoom Long roomId,
                                                                                      @RequestParam(required = false) List<Double> cursorIds,
                                                                                      @PageableDefault(size = 10, sort = {"id", "likeCnt"}, page = 0, direction = Sort.Direction.DESC) Pageable pageable) {
        if (cursorIds == null) cursorIds = new ArrayList<>();
        List<Review> reviewList = reviewService.getReviewListByRoomId(roomId, cursorIds, pageable);
        List<ReviewResponseDto.ReviewListDto> reviewListResponseList =
                reviewList.stream()
                        .map(ReviewSerializer::toReviewListDto)
                        .collect(Collectors.toList());
        return ResponseEntity.ok(QueryDslUtil.toSlice(reviewListResponseList, pageable));
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
    public ResponseEntity<ReviewResponseDto.ReviewCreateDto> createReview(@ModelAttribute @Valid ReviewRequestDto.ReviewCreateDto request, @AuthenticationPrincipal MemberDto authentication, @AuthUser Member loginMember) {
        Review review = reviewService.saveReview(request, loginMember);

        return ResponseEntity.ok(ReviewResponseDto.ReviewCreateDto.builder()
                .reviewId(review.getId())
                .createdAt(LocalDateTime.now())
                .build());
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
    public ResponseEntity<ReviewResponseDto.ReviewDeleteDto> deleteReview(@PathVariable("reviewId") @ExistReview Long reviewId) {
        Long deletedReviewId = reviewService.deleteById(reviewId);
        return ResponseEntity.ok(ReviewResponseDto.ReviewDeleteDto.builder()
                .reviewId(deletedReviewId)
                .deletedAt(LocalDateTime.now())
                .affectedRowCnt(3) // 어캐앎 ?? todo : affected row 하드코딩 해결 및 review API 전부 테스트
                .build());
    }
}
