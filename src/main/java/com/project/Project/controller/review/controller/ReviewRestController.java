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
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
public class ReviewRestController {
    /* todo
        @GetMapping("/building/room/review")
     */

    @GetMapping("/buildig/{buildingId}/room/review")
    public List<ReviewResponseDto.ReviewListResponse> getReviewListByBuilding(@PathVariable("buildingId") @ExistBuilding Integer buildingId){

        return null;
    }

    @GetMapping("/building/room/{roomId}/review")
    public List<ReviewResponseDto.ReviewListResponse> getReviewListByRoom(@PathVariable("roomId") @ExistRoom Integer roomId){
        return null;
    }
    /* todo
        @GetMapping("/building/room/review/{reviewId}")
    */

    @PostMapping("/building/room/review")
    public ReviewResponseDto.ReviewCreateResponse createReview(@RequestBody @Valid ReviewRequestDto.ReviewCreateDto request){
        return null;
    }
    /* todo
        @PutMapping("/building/room/review/{reviewId}")
     */

    @DeleteMapping("/building/room/review/{reviewId}")
    public ReviewResponseDto.ReviewDeleteResponse deleteReview(@PathVariable("reviewID") @ExistReview Integer reviewId){
        return null;
    }
}
