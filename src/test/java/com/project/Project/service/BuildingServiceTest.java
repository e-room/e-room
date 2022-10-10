package com.project.Project.service;

import com.project.Project.Util.BuildingGenerator;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.RoomRepository;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.serializer.review.ReviewSerializer;
import com.project.Project.service.impl.BuildingServiceImpl;
import com.project.Project.service.impl.RoomServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@Import(BuildingGeneratorTestConfig.class)
public class BuildingServiceTest {
    @Mock
    private BuildingRepository buildingRepository;
    @Mock
    private BuildingCustomRepository buildingCustomRepository;

    @Spy
    private WebClient webClient;

    private BuildingGenerator buildingGenerator;
    @InjectMocks
    private BuildingServiceImpl buildingService;

    @BeforeEach
    public void prepare(){
        this.buildingGenerator = new BuildingGenerator(webClient,buildingCustomRepository,buildingRepository);
    }

    // todo : createBuilding 구현 후 작성
    @Test
    void createBuilding_Test() {
        Address address = Address.builder()
                .siDo("경기도")
                .siGunGu("수원시 영통구")
                .roadName("매영로425번길")
                .eupMyeon("")
                .buildingNumber("4")
                .build();
        this.buildingGenerator.init();
        Building building = buildingService.createBuilding(address);
    }

}
