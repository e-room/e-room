package com.project.Project.repository;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.room.Room;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BuildingRepositoryTest {

    BuildingRepository buildingRepository;
    RoomRepository roomRepository;

    Building testBuilding1;
    Building saveBuilding1;
    Building testBuilding2;
    Building saveBuilding2;

    Room testRoom1;
    Room testRoom2;
    Room testRoom3;
    Room testRoom4;

    @Autowired
    public BuildingRepositoryTest(BuildingRepository buildingRepository, RoomRepository roomRepository) {
        this.buildingRepository = buildingRepository;
        this.roomRepository = roomRepository;
    }

    @BeforeEach
    void setup(){
        testBuilding1 = Building.builder().hasElevator(true).address(Address.builder().metropolitanGovernment("대전광역시").siGunGu("유성구").roadName("대학로 291").build()).buildingName("덕영빌").build();
        testBuilding2 = Building.builder().hasElevator(false).address(Address.builder().metropolitanGovernment("서울특별시").siGunGu("관악구").roadName("덕영대로 47").build()).buildingName("휴먼라이트 빌").build();
        buildingRepository.save(testBuilding1);
        buildingRepository.save(testBuilding2);
        testRoom1 = Room.builder()
                .roomNumber(101).lineNumber(1).build();
        testRoom2 = Room.builder()
                .roomNumber(102).lineNumber(1).build();
        testRoom3 = Room.builder()
                .roomNumber(101).lineNumber(1).build();
        testRoom4 = Room.builder()
                .roomNumber(102).lineNumber(1).build();
        testBuilding1.addRooms(Arrays.asList(testRoom1,testRoom2));
        testBuilding2.addRooms(Arrays.asList(testRoom3,testRoom4));
        saveBuilding1 = buildingRepository.save(testBuilding1);
        saveBuilding2 = buildingRepository.save(testBuilding2);
    }

    @AfterEach
    void cleanup(){
        this.buildingRepository.deleteAll();
        this.roomRepository.deleteAll();
    }
    @Test
    void findBuildingByIdTest() {

        Building expected1 = saveBuilding1;
        Building result1 = this.buildingRepository.findBuildingById(saveBuilding1.getId());
        assertThat(result1)
                .usingRecursiveComparison().isEqualTo(expected1);

        Building expected2 = testBuilding2;
        Building result2 = this.buildingRepository.findBuildingById(saveBuilding2.getId());
        assertThat(result2)
                .usingRecursiveComparison().isEqualTo(expected2);
    }

    @Test
    void findBuildingsByIdsTest() {
        List<Building> expectedList = Arrays.asList(saveBuilding1, saveBuilding2);
        List<Building> resultList = this.buildingRepository.findBuildingsByIdIn(Arrays.asList(saveBuilding1.getId(), saveBuilding2.getId()));
        assertThat(resultList).usingRecursiveComparison().isEqualTo(expectedList);
    }

    @Test
    void searchBuildingTest() {
        List<Building> expectedList = Arrays.asList(saveBuilding1, saveBuilding2);
        List<Building> resultList = this.buildingRepository.searchBuilding("덕영");
        assertThat(resultList).usingRecursiveComparison().isEqualTo(expectedList);
    }
}
