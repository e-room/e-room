package com.project.Project.repository;

import com.project.Project.domain.member.Member;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.enums.MemberRole;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.interaction.FavoriteRepository;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewKeywordService;
import com.project.Project.unit.repository.RepositoryTestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewKeywordService.class, ReviewCategoryService.class}))
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(RepositoryTestConfig.class)
public class FavoriteRepositoryTest {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    Member member1, member2;
    Building building1, building2, building3;
    Favorite favorite1, favorite2, favorite3;

    @BeforeEach
    void setup() {
        member1 = Member.builder() // temp user
                .reviewList(new ArrayList<>())
                .favoriteBuildingList(new ArrayList<>())
                .reviewLikeList(new ArrayList<>())
                .name("하품하는 망아지")
                .email("swa07016@khu.ac.kr")
                .memberRole(MemberRole.USER)
                .refreshToken("mockingMember")
                .build();

        member2 = Member.builder() // temp user
                .reviewList(new ArrayList<>())
                .favoriteBuildingList(new ArrayList<>())
                .reviewLikeList(new ArrayList<>())
                .name("귀여운 고양이")
                .email("swa07016@khu.ac.kr")
                .memberRole(MemberRole.USER)
                .refreshToken("mockingMember")
                .build();


        memberRepository.save(member1);
        memberRepository.save(member2);

        building1 = Building.builder().favoriteList(new ArrayList<>()).hasElevator(true).address(Address.builder().siDo("대전광역시").siGunGu("유성구").roadName("대학로").buildingNumber("291").build()).buildingName("덕영빌").coordinate(new Coordinate(34.2321, 40.1)).build();
        building2 = Building.builder().favoriteList(new ArrayList<>()).hasElevator(false).address(Address.builder().siDo("서울특별시").siGunGu("관악구").roadName("덕영대로").buildingNumber("47").build()).buildingName("휴먼라이트 빌").coordinate(new Coordinate(45.2321, 50.1)).build();
        building3 = Building.builder().favoriteList(new ArrayList<>()).hasElevator(false).address(Address.builder().siDo("경기도").siGunGu("수원시 영통구").roadName("덕영대로").buildingNumber("47").build()).buildingName("구찌빌").coordinate(new Coordinate(36.2321, 120.1)).build();
        buildingRepository.save(building1);
        buildingRepository.save(building2);
        buildingRepository.save(building3);


        favorite1 = Favorite.builder().build();
        favorite2 = Favorite.builder().build();
        favorite3 = Favorite.builder().build();
        favorite1.setBuilding(building1);
        favorite1.setMember(member1);
        favorite2.setBuilding(building2);
        favorite2.setMember(member2);
        favorite3.setBuilding(building3);
        favorite3.setMember(member1);
        favoriteRepository.save(favorite1);
        favoriteRepository.save(favorite2);
        favoriteRepository.save(favorite3);

    }

    @AfterEach
    void cleanup() {
        favoriteRepository.deleteAll();
        memberRepository.deleteAll();
        buildingRepository.deleteAll();
    }

    @Test
    void findByBuildingAndMember_Test() {
        Favorite expected = favorite1;
        Favorite result = favoriteRepository.findByBuildingAndMember(building1, member1);

        assertThat(result)
                .usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void existsByMemberAndBuilding_Id_Test() {
        boolean expected = true;
        boolean result = favoriteRepository.existsByMemberAndBuilding_Id(member2, building2.getId());

        assertThat(result)
                .isEqualTo(expected);
    }

    @Test
    void findByMember_Test() {
        List<Favorite> expected = new ArrayList<>();
        expected.add(favorite1);
        expected.add(favorite3);


        List<Favorite> result = favoriteRepository.findByMember(member1);
        assertThat(result)
                .usingRecursiveComparison().isEqualTo(expected);
    }

}
