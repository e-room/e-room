package com.project.Project.repository;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BuildingRepositoryTest {

    @Autowired
    private BuildingRepository buildingRepository;

    @Test
    void findByAddressTest() throws Exception {
        // TEST Address : 경기도 수원시 영통구 매영로425번길 4(영통동)

        // given
        String requestAddress = "경기도 수원시 영통구 매영로425번길 4(영통동)";
        Address userAddress = Address.valueOf(requestAddress);

        Address address = Address.builder()
                .metropolitanGovernment("경기도")
                .basicLocalGovernment("수원시")
                .siGunGu("영통구")
                .eupMyeon("")
                .roadName("매영로425번길")
                .buildingNumber("4")
                .build();

        Coordinate coordinate = Coordinate.builder()
                .latitude(36.123132)
                .longitude(121.152215)
                .build();

        Building newBuilding = Building.builder()
                .address(address)
                .coordinate(coordinate)
                .hasElevator(true)
                .roomList(new ArrayList<>())
                .memberList(new ArrayList<>())
                .build();

        Building savedBuilding = buildingRepository.save(newBuilding);

        // when
        Building building = buildingRepository.findByAddress(userAddress).orElseThrow(() -> new RuntimeException("Bulilding Not Found!"));

        // then
        assertEquals(savedBuilding, building);
    }
}
