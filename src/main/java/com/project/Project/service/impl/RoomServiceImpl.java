package com.project.Project.service.impl;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.RoomRepository;
import com.project.Project.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    public Room createRoom(Building building, Integer lineNumber, Integer roomNumber) {
        Room room = Room.builder()
                .reviewList(new ArrayList<>())
                .lineNumber(lineNumber)
                .roomNumber(roomNumber)
                .build();
        room.setBuilding(building);
        roomRepository.save(room);
        return room;
    }

    public Optional<Room> findByBuildingAndLineNumberAndRoomNumber(Building building, Integer lineNumber, Integer roomNumber) {
        return roomRepository.findByBuildingAndLineNumberAndRoomNumber(building, lineNumber, roomNumber);
    }
}
