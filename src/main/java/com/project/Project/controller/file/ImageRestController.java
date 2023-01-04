package com.project.Project.controller.file;

import com.project.Project.controller.file.dto.ThumbnailResponseDto;
import com.project.Project.controller.review.dto.ReviewResponseDto;
import com.project.Project.domain.Thumbnail;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.exception.CustomException;
import com.project.Project.exception.ErrorCode;
import com.project.Project.serializer.file.FileSerializer;
import com.project.Project.serializer.review.ReviewSerializer;
import com.project.Project.service.ThumbnailImageService;
import com.project.Project.service.review.ReviewImageService;
import com.project.Project.validator.ExistReviewImage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@Tag(name = "Image API", description = "이미지 썸네일 생성, 이미지 단건 조회")
@RestController
@RequiredArgsConstructor
public class ImageRestController {

    private final ThumbnailImageService thumbnailImageService;

    private final ReviewImageService reviewImageService;

    @GetMapping("/image")
    public ResponseEntity<ReviewResponseDto.ReviewImageDto> getReviewImageByUuid(@RequestParam("uuid") @ExistReviewImage String uuid) {
        ReviewImage reviewImage = reviewImageService.findByUuid(uuid);
        return ResponseEntity.ok(ReviewSerializer.toReviewImageDto(reviewImage));
    }

    @Deprecated
    @PostMapping("/images/thumbnail")
    public ResponseEntity<ThumbnailResponseDto.ThumbnailResponseList> uploadFile(@RequestParam("images") List<MultipartFile> uploadFiles) {

        /*
        @todo: validator로 빼기
         */
        for (MultipartFile uploadFile : uploadFiles) {

            // 이미지 파일만 업로드 가능
            if (uploadFile.getContentType().startsWith("image") == false) {
                // 이미지가 아닌경우 403 Forbidden 반환
                throw new CustomException(ErrorCode.IMAGE_ONLY);
            }
        }

        List<Thumbnail> thumbnailList = thumbnailImageService.saveThumbnailList(uploadFiles);
        List<ThumbnailResponseDto.ThumbnailResponse> thumbnailResponseList = thumbnailList.stream().map(FileSerializer::toThumbnailResponse).collect(Collectors.toList());

        return ResponseEntity.ok(ThumbnailResponseDto.ThumbnailResponseList.builder()
                .thumbnailList(thumbnailResponseList)
                .totalCount(Long.valueOf(thumbnailList.size()))
                .build());
    }

}
