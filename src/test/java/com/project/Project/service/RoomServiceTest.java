package com.project.Project.service;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.RoomRepository;
import com.project.Project.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    public void createRoom_Test() {
        // given
        Building building = Building.builder().build();
        given(roomRepository.save(any()))
                .willReturn(Room.builder().lineNumber(101).build());

        // when
        Room room = roomService.createRoom(building, 101, 103);

        // then
        assertEquals(Integer.valueOf(101), room.getLineNumber());
    }
}
