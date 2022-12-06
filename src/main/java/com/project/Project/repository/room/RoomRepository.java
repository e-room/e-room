package com.project.Project.repository.room;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select distinct r from Room r join fetch r.reviewList where r.building = :building")
    List<Room> findByBuilding(Building building);

    Optional<Room> findByBuildingAndLineNumberAndRoomNumber(Building building, Integer lineNumber, Integer roomNumber);
}
