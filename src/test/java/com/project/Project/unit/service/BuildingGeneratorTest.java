package com.project.Project.unit.service;

import com.project.Project.config.WebClientConfig;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.service.building.BuildingGenerator;
import com.project.Project.util.KakaoAddressAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {BuildingGenerator.class})
@Import({WebClientConfig.class})
public class BuildingGeneratorTest {
    @Autowired
    @Qualifier("kakaoMapWebClient")
    private WebClient kakaoMapClient;

    @MockBean
    private BuildingCustomRepository buildingCustomRepository;
    @MockBean
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingGenerator buildingGenerator = new BuildingGenerator(kakaoMapClient, buildingCustomRepository, buildingRepository);

    @Test
    public void searchBuildingByKakao() {

    }

    @BeforeEach
    public void prepare() {
        this.buildingGenerator.init();
    }

    // todo : createBuilding 구현 후 작성
    @Test
    void searchAddress_Test() {
        Address address = Address.builder()
                .siDo("경기도")
                .siGunGu("수원시 영통구")
                .roadName("매영로425번길")
                .eupMyeon("")
                .buildingNumber("4")
                .build();
        KakaoAddressAPI addressAPI = BuildingGenerator.searchAddressByKakao(address.toString());

    }

    @Test
    void generateBuilding_Test() {
        Address address = Address.builder()
                .siDo("경기도")
                .siGunGu("수원시 영통구")
                .roadName("매영로425번길")
                .eupMyeon("")
                .buildingNumber("4")
                .build();
        Building building = BuildingGenerator.generateBuilding(address);
        Building expected = Building.builder()
                .address(address).build();
        EqualBuilding(building, expected);
        assertThat(building.getCoordinate()).isNotNull();
    }

    private Boolean EqualBuilding(Building building1, Building building2) {
        Address address1 = building1.getAddress();
        Address address2 = building2.getAddress();
        assertThat(address1).usingRecursiveComparison().isEqualTo(address2);
        return true;
    }

    @Test
    void searchBuilding_Test() {
        Address address = Address.builder()
                .siDo("경기도")
                .siGunGu("수원시 영통구")
                .roadName("매영로425번길")
                .eupMyeon("")
                .buildingNumber("4")
                .build();
        List<Building> buildingList = BuildingGenerator.generateBuildings(address.toString());
    }
}
