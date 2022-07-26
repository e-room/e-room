package com.project.Project.controller.room;

import com.project.Project.controller.room.dto.RoomResponseDto;
import com.project.Project.domain.Room;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomRestController {

    @GetMapping("/building/room/{roomId}")
    public RoomResponseDto.RoomResponse getRoom(@PathVariable("roomId")Integer roomId){
        /*

         */
    }

    /* todo
        @PostMapping("/building/{buildingId}/room")
        public Room getRoom
     */
}
