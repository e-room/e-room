package com.project.Project.common.serializer.file;

import com.project.Project.controller.file.dto.ThumbnailResponseDto;
import com.project.Project.domain.Thumbnail;

public class FileSerializer {

    public static ThumbnailResponseDto.ThumbnailResponse toThumbnailResponse(Thumbnail thumbnail) {
        return ThumbnailResponseDto.ThumbnailResponse.builder()
                .id(thumbnail.getId())
                .uuid(thumbnail.getUuid().getUuid())
                .url(thumbnail.getUrl())
                .fileName(thumbnail.getFileName())
                .build();
    }
}
