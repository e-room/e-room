package com.project.Project.controller.review.controller;

import com.project.Project.Util.JsonResult;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.validator.ExistBuilding;
import com.project.Project.validator.ExistReview;
import com.project.Project.validator.ExistRoom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
public class ReviewRestController {
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

        return null;
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
        return null;
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
        return null;
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
        return null;
    }
}
