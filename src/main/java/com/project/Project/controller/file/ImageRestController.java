package com.project.Project.controller.file;

import com.project.Project.controller.file.dto.ThumbnailResponseDto;
import com.project.Project.domain.Thumbnail;
import com.project.Project.exception.CustomException;
import com.project.Project.exception.ErrorCode;
import com.project.Project.serializer.file.FileSerializer;
import com.project.Project.service.ThumbnailImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ImageRestController {

    private final ThumbnailImageService thumbnailImageService;

    @PostMapping("/thumbnail")
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
