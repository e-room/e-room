package com.project.Project.serializer.room;

import com.project.Project.controller.room.dto.RoomResponseDto;
import com.project.Project.domain.room.Room;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.room.RoomException;

import java.util.Optional;

public class RoomSerializer {

    public static RoomResponseDto.RoomListDto toRoomListResponse(Room room) {
        Optional.ofNullable(room).orElseThrow(() -> new RoomException(ErrorCode.ROOM_NPE));
        return RoomResponseDto.RoomListDto.builder()
                .roomId(room.getId())
                .roomNumber(room.getRoomNumber())
                .build();
    }

    public static RoomResponseDto.BaseRoomDto toBaseRoomResponse(Room room) {
        Optional.ofNullable(room).orElseThrow(() -> new RoomException(ErrorCode.ROOM_NPE));
        return RoomResponseDto.BaseRoomDto.builder()
                .roomId(room.getId())
                .lineNumber(room.getLineNumber())
                .roomNumber(room.getRoomNumber())
                .build();
    }
}
