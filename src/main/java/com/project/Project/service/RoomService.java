package com.project.Project.service;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.room.Room;

import java.util.Optional;

public interface RoomService {
    Room createRoom(Building building, Integer lineNumber, Integer roomNumber);
    Optional<Room> findByBuildingAndLineNumberAndRoomNumber(Building building, Integer lineNumber, Integer roomNumber);
}
