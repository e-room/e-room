package com.project.Project.serializer.room;

import com.project.Project.controller.room.dto.RoomResponseDto;
import com.project.Project.domain.room.Room;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.room.RoomException;

import java.util.Optional;

public class RoomSerializer {

    public static RoomResponseDto.RoomListResponse toRoomListResponse(Room room) {
        Optional.ofNullable(room).orElseThrow(() -> new RoomException(ErrorCode.ROOM_NPE));
        return RoomResponseDto.RoomListResponse.builder()
                .roomId(room.getId())
                .roomNumber(room.getRoomNumber())
                .build();
    }
}
