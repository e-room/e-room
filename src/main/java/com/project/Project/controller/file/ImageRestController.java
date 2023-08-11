package com.project.Project.controller.file;

import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.common.serializer.review.ReviewSerializer;
import com.project.Project.service.ThumbnailImageService;
import com.project.Project.service.review.ReviewImageService;
import com.project.Project.common.validator.ExistReviewImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Tag(name = "Image API", description = "이미지 썸네일 생성, 이미지 단건 조회")
@RestController
@RequiredArgsConstructor
public class ImageRestController {

    private final ThumbnailImageService thumbnailImageService;

    private final ReviewImageService reviewImageService;

    @Operation(summary = "이미지 단건 조회", description = "이미지 단건 조회 by Uuid API")
    @Parameter(name = "uuid", description = "조회하고자 하는 이미지의 uuid", example = "5c426f33-1845-4088-a90c-c6f37853c4f2")
    @GetMapping("/image")
    public ResponseEntity<ReviewResponseDto.ReviewImageDto> getReviewImageByUuid(@RequestParam("uuid") @ExistReviewImage String uuid) {
        ReviewImage reviewImage = reviewImageService.findByUuid(uuid);
        return ResponseEntity.ok(ReviewSerializer.toReviewImageDto(reviewImage));
    }

}
