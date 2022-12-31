package com.project.Project.controller.file.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ThumbnailResponseDto {

    @Builder
    @Getter
    public static class ThumbnailResponse {
        private Long id;
        private String fileName;
        private String uuid;
        private String url;
    }


    @Builder
    @Getter
    public static class ThumbnailResponseList {
        private Long totalCount;
        private List<ThumbnailResponse> thumbnailList;
    }
}
