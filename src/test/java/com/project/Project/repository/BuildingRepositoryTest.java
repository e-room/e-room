package com.project.Project.repository;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.projection.building.OnlyBuildingIdAndCoord;
import com.project.Project.repository.room.RoomRepository;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewKeywordService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewKeywordService.class, ReviewCategoryService.class})
)
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
@AutoConfigureMockMvc
public class BuildingRepositoryTest {

    @Mock
    ReviewCategoryService reviewCategoryService;

    @Mock
    ReviewKeywordService reviewKeywordService;

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
    void setup() {
        testBuilding1 = Building.builder().hasElevator(true).address(Address.builder().siDo("대전광역시").siGunGu("유성구").roadName("대학로").buildingNumber("291").build()).buildingName("덕영빌").coordinate(new Coordinate(34.2321, 40.1)).build();
        testBuilding2 = Building.builder().hasElevator(false).address(Address.builder().siDo("서울특별시").siGunGu("관악구").roadName("덕영대로").buildingNumber("47").build()).buildingName("휴먼라이트 빌").coordinate(new Coordinate(45.2321, 50.1)).build();
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
        testBuilding1.addRooms(Arrays.asList(testRoom1, testRoom2));
        testBuilding2.addRooms(Arrays.asList(testRoom3, testRoom4));
        saveBuilding1 = buildingRepository.save(testBuilding1);
        saveBuilding2 = buildingRepository.save(testBuilding2);
    }

    @AfterEach
    void cleanup() {
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
        List<Building> resultList = this.buildingRepository.searchBuildings("덕영");
        assertThat(resultList).usingRecursiveComparison().isEqualTo(expectedList);
    }

    @Test
    void findProjectedByTest() {
        List<OnlyBuildingIdAndCoord> expectedList = Arrays.asList(saveBuilding1, saveBuilding2).stream().map((elem) -> OnlyBuildingIdAndCoord.builder().id(elem.getId()).coordinate(elem.getCoordinate()).build()).collect(Collectors.toList());
        List<OnlyBuildingIdAndCoord> resultList = this.buildingRepository.findBy(OnlyBuildingIdAndCoord.class);

        assertThat(resultList).usingRecursiveComparison().isEqualTo(expectedList);
    }

    @Test
    void findBuildingByAddressTest() {
        Building expectedBuilding = saveBuilding1;
        List<Building> results = this.buildingRepository.findAll();
        Building result = this.buildingRepository.findBuildingByAddress(Address.builder().siDo("대전광역시").siGunGu("유성구").roadName("대학로").buildingNumber("291").build()).get();
        assertThat(result)
                .usingRecursiveComparison().isEqualTo(expectedBuilding);
    }
}
