package com.project.Project.service;

import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.BuildingRepository;
import com.project.Project.repository.RoomRepository;
import com.project.Project.serializer.review.ReviewSerializer;
import com.project.Project.service.impl.BuildingServiceImpl;
import com.project.Project.service.impl.RoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BuildingServiceTest {
    @Mock
    private BuildingRepository buildingRepository;

    @InjectMocks
    private BuildingServiceImpl buildingService;

    // todo : createBuilding 구현 후 작성
    @Test
    void createBuilding_Test() {



    }
}
