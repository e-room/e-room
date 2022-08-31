package com.project.Project.controller.review.controller;

import com.project.Project.Util.JsonResult;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.enums.MemberRole;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import com.project.Project.service.BuildingService;
import com.project.Project.service.ReviewService;
import com.project.Project.service.RoomService;
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
    private final BuildingService buildingService;
    private final RoomService roomService;

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
        Member member = Member.builder() // temp user
                .reviewList(new ArrayList<>())
                .favoriteBuildingList(new ArrayList<>())
                .likeReviewList(new ArrayList<>())
                .name("하품하는 망아지")
                .email("swa07016@khu.ac.kr")
                .memberRole(MemberRole.USER)
                .profileImageUrl("https://lh3.googleusercontent.com/ogw/AOh-ky20QeRrWFPI8l-q3LizWDKqBpsWTIWTcQa_4fh5=s64-c-mo")
                .build();
        Long savedReviewId = 0L;
        Optional<Building> building = buildingService.findByAddress(request.getAddress());

        if(building.isPresent()) { // building이 존재할 때
            // room이 존재하는 경우 : 존재하는 room연관관계 맺은 review 저장
            Optional<Room> room = roomService.findByBuildingAndLineNumberAndRoomNumber(
                    building.get(), request.getRoomNumber(), request.getLineNumber());
            if(room.isPresent()) {
                Review review = request.toReview(member, room.get());
                savedReviewId = reviewService.save(review);
                // todo : 정상 응답
            }
            // room이 존재하지 않는 경우 : room을 생성해준 후 review 저장
            else {
                Room newRoom = roomService.createRoom(building.get(), request.getLineNumber(), request.getRoomNumber());
                Review review = request.toReview(member, newRoom);
                savedReviewId = reviewService.save(review);
            }
        } else { // building이 없을 때 : 해당하는 주소에 집이 없다고 알려줌
            /*
             TODO
             여기서 어떻게 처리해야하지?
             ReviewResponseDto.ReviewCreateResponse 반환할 수는 없으니까
             BuildingNotFoundException 같은 걸 만들어서 던지고 컨트롤러 어드바이스로 핸들링 해야하나?
            * */
        }

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
