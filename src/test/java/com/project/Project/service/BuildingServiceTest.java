package com.project.Project.service;

import com.project.Project.controller.building.dto.BuildingOptionalDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.service.building.impl.BuildingServiceImpl;
import com.project.Project.util.component.BuildingGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

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
    public void prepare() {
        this.buildingGenerator = new BuildingGenerator(webClient, buildingCustomRepository, buildingRepository);
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
        BuildingOptionalDto dto = new BuildingOptionalDto("원빌리지", false);
        Building building = buildingService.createBuilding(address, dto);
    }

}
