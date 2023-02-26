package com.project.Project.controller.review.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.building.dto.BuildingOptionalDto;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.review.ReviewException;

import com.project.Project.serializer.review.ReviewSerializer;
import com.project.Project.service.building.BuildingService;
import com.project.Project.service.member.MemberService;
import com.project.Project.service.review.ReviewImageService;
import com.project.Project.service.review.ReviewService;
import com.project.Project.util.component.QueryDslUtil;
import com.project.Project.validator.ExistBuilding;
import com.project.Project.validator.ExistReview;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.annotation.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Review API", description = "리뷰 등록, 조회, 삭제")
@Validated
@RestController
@RequiredArgsConstructor
public class ReviewRestController {

    private final ReviewService reviewService;
    private final BuildingService buildingService;
    private final ReviewImageService reviewImageService;
    private final MemberService memberService;

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
    @Operation(summary = "리뷰 조회 by 건물 [3.2]", description = "특정 건물에 대한 리뷰 리스트를 반환 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @Parameters({
            @Parameter(name = "buildingId", description = "리뷰를 얻고자 하는 건물의 id", example = "123"),
            @Parameter(name = "cursorIds", description = "커서 id", example = "2.4,8714"),
            @Parameter(name = "size", description = "응답 리뷰 개수", example = "4"),
            @Parameter(name = "sort", description = "정렬 기준", example = "id,DESC"),
            @Parameter(name = "pageable", hidden = true)
    })
    @GetMapping("/building/{buildingId}/room/review")
    public ResponseEntity<ReviewResponseDto.ReviewSliceDto> getReviewListByBuilding(@PathVariable("buildingId") @ExistBuilding Long buildingId,
                                                                                    @RequestParam(required = false) List<Double> cursorIds,
                                                                                    @PageableDefault(size = 10, sort = {"id"}, page = 0, direction = Sort.Direction.DESC) Pageable pageable,
                                                                                    @AuthUser Member member) {
        if (cursorIds == null) cursorIds = new ArrayList<>();
        List<Review> reviewList = reviewService.getReviewListByBuildingId(buildingId, cursorIds, pageable);
        List<ReviewLike> reviewLikeList = memberService.getReviewLikeList(member);
        List<ReviewResponseDto.ReviewDto> reviewListResponseList =
                reviewList.stream()
                        .map(ReviewSerializer::toReviewDto)
                        .map((dto) -> ReviewSerializer.setIsLiked(dto, reviewLikeList))
                        .collect(Collectors.toList());
        Boolean needToBlur = false;
        if (member == null || memberService.getReviewCnt(member) < 1) {
            needToBlur = true;
            if(reviewList.size() >= 1) reviewListResponseList = reviewListResponseList.subList(0, 1);
        }
        Slice<ReviewResponseDto.ReviewDto> slicedReviewList = QueryDslUtil.toSlice(reviewListResponseList, pageable);

        ReviewResponseDto.ReviewSliceDto responseBody = ReviewResponseDto.ReviewSliceDto.builder().reviewSlicedList(slicedReviewList).needToBlur(needToBlur).build();
        return ResponseEntity.ok(responseBody);
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
    @Operation(summary = "리뷰 등록 [7.4]", description = "리뷰 등록 API<br>" +
            "요청 예시는 <a href='https://e-room.postman.co/workspace/Team-Workspace~86a5982f-98d2-4698-8cf7-b1231d296257/overview'>E-room 포스트맨</a> 참조")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReviewResponseDto.ReviewCreateDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "authentication", hidden = true),
            @Parameter(name = "loginMember", hidden = true)
    })
    @PostMapping(value = "/building/room/review", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    // multipart/form-data 형태로 받음
    public ResponseEntity<ReviewResponseDto.ReviewCreateDto> createReview(@RequestPart @Valid ReviewRequestDto.ReviewCreateDto request, @RequestPart @Size(max = 5) @Nullable List<MultipartFile> reviewImageList, @AuthUser Member loginMember) {

        Address address = AddressDto.toAddress(request.getAddress());
        BuildingOptionalDto buildingOptionalDto = request.getBuildingOptionalDto();

        Building building = buildingService.createBuilding(address, buildingOptionalDto);// 빌딩이 없는 경우 생성

        if (!(reviewImageList == null || reviewImageList.isEmpty())) {
            request.setReviewImageList(reviewImageList);
        }
        Review review = reviewService.saveReview(request, loginMember, building);
        Boolean isFirstReview = loginMember.getReviewList().size() >= 2 ? false : true;
        return ResponseEntity.ok(ReviewSerializer.toReviewCreateDto(review.getId(), review.getBuilding().getId(), isFirstReview));
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
    @Operation(summary = "리뷰 삭제 [3.2.2]", description = "리뷰 삭제 API<br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReviewResponseDto.ReviewDeleteDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "reviewId", description = "삭제하고자 하는 리뷰의 id")
    })
    @DeleteMapping("/building/room/review/{reviewId}")
    public ResponseEntity<ReviewResponseDto.ReviewDeleteDto> deleteReview(@PathVariable("reviewId") @ExistReview Long reviewId, @AuthUser Member loginMember) {
        Review target = reviewService.getReviewById(reviewId);
        if (!target.getAuthor().getId().equals(loginMember.getId())) {
            throw new ReviewException("다른 사람의 review를 삭제할 수 없습니다.", ErrorCode.REVIEW_ACCESS_DENIED);
        }
        Long deletedReviewId = reviewService.deleteById(reviewId);
        return ResponseEntity.ok(ReviewSerializer.toReviewDeleteDto(deletedReviewId));
    }

    @Deprecated
    @Operation(summary = "리뷰 이미지 조회 [3.2]", description = "리뷰 이미지 조회 API<br>")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReviewResponseDto.ReviewImageListDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "reviewId", description = "이미지를 조회하고자 하는 리뷰의 id")
    })
    @GetMapping("/review/{reviewId}/images")
    public ResponseEntity<ReviewResponseDto.ReviewImageListDto> getReviewImageList(@PathVariable("reviewId") @ExistReview Long reviewId) {
        List<ReviewImage> reviewImageList = reviewImageService.findByReview(reviewId);
        return ResponseEntity.ok(ReviewSerializer.toReviewImageListDto(reviewImageList));
    }
}
