package com.project.Project.controller.room;

import com.project.Project.controller.room.dto.RoomResponseDto;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.serializer.room.RoomSerializer;
import com.project.Project.service.review.ReviewImageService;
import com.project.Project.validator.ExistRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/room/{roomId}/images")
    public ResponseEntity<RoomResponseDto.RoomImageListDto> getRoomImageList(@PathVariable("roomId") @ExistRoom Long roomId) {
        List<ReviewImage> reviewImageList = reviewImageService.findByRoom(roomId);
        return ResponseEntity.ok(RoomSerializer.toRoomImageListDto(reviewImageList));
    }
}
