package com.project.Project.serializer.interaction;

import com.project.Project.controller.interaction.dto.FavoriteResponseDto;

import java.time.LocalDateTime;

public class FavoriteSerializer {
    public static FavoriteResponseDto.FavoriteAddResponse toFavoriteAddResponse(Long savedFavoriteId) {
        return FavoriteResponseDto.FavoriteAddResponse.builder()
                .favoriteId(savedFavoriteId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static FavoriteResponseDto.FavoriteDeleteResponse toFavoriteDeleteResponse(Long deletedFavoriteId) {
        return FavoriteResponseDto.FavoriteDeleteResponse.builder()
                .favoriteId(deletedFavoriteId)
                .deletedAt(LocalDateTime.now())
                .build();
    }
}
