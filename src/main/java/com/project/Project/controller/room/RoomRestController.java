package com.project.Project.controller.room;

import com.project.Project.controller.interaction.dto.FavoriteResponseDto;
import com.project.Project.controller.room.dto.RoomResponseDto;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.serializer.room.RoomSerializer;
import com.project.Project.service.review.ReviewImageService;
import com.project.Project.validator.ExistRoom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Room API", description = "리뷰 이미지 조회 by 방")
@Validated
@RestController
@RequiredArgsConstructor
public class RoomRestController {

    private final ReviewImageService reviewImageService;

    /*
        todo later
    @GetMapping("/building/room/{roomId}")
    public RoomResponseDto.RoomResponse getRoom(@PathVariable("roomId")Integer roomId){
    }
    */

    /* todo later
        @PostMapping("/building/{buildingId}/room")
        public Room getRoom
     */

    @Operation(summary = "방에 대한 리뷰 이미지 조회 [3.2]", description = "리뷰 이미지 조회 by roomId API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = RoomResponseDto.ReviewImageListDto.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "roomId", description = "이미지를 조회하고자하는 방의 id", example = "1004")
    })
    @GetMapping("/room/{roomId}/images")
    public ResponseEntity<RoomResponseDto.ReviewImageListDto> getRoomImageList(@PathVariable("roomId") @ExistRoom Long roomId) {
        List<ReviewImage> reviewImageList = reviewImageService.findByRoom(roomId);
        return ResponseEntity.ok(RoomSerializer.toReviewImageListDto(reviewImageList));
    }
}
