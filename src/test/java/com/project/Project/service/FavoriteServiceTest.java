package com.project.Project.service;

import com.project.Project.domain.member.Member;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.embedded.Coordinate;
import com.project.Project.domain.enums.MemberRole;
import com.project.Project.domain.interaction.Favorite;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.interaction.FavoriteRepository;
import com.project.Project.service.impl.FavoriteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private BuildingRepository buildingRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private BuildingCustomRepository buildingCustomRepo;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private Member createTestMember() {
        return Member.builder() // temp user
                .reviewList(new ArrayList<>())
                .favoriteBuildingList(new ArrayList<>())
                .reviewLikeList(new ArrayList<>())
                .name("하품하는 망아지")
                .email("swa07016@khu.ac.kr")
                .memberRole(MemberRole.USER)
                .refreshToken("mockingMember")
                // .profileImageUrl("https://lh3.googleusercontent.com/ogw/AOh-ky20QeRrWFPI8l-q3LizWDKqBpsWTIWTcQa_4fh5=s64-c-mo")
                .build();
    }

    private Building testBuilding = Building.builder().favoriteList(new ArrayList<>()).hasElevator(true).address(Address.builder().siDo("대전광역시").siGunGu("유성구").roadName("대학로").buildingNumber("291").build()).buildingName("덕영빌").coordinate(new Coordinate(34.2321, 40.1)).build();


    @Test
    void addFavoriteBuilding_Test() {
        // given
        Member member = createTestMember();
        given(buildingRepository.findBuildingById(any()))
                .willReturn(testBuilding);
        given(favoriteRepository.save(any()))
                .willReturn(Favorite.builder().id(2L).build());

        // when
        Long savedFavoriteId = favoriteService.addFavoriteBuilding(1L, member);

        // then
        assertNotNull(savedFavoriteId);
    }

//    @Test
//    void getBuildingListByMember_Test() {
//        // given
//        Member member = createTestMember();
//        CursorDto cursorDto = new CursorDto();
//        cursorDto.setCursorId(4L); cursorDto.setSize(5);
//        Building building1 = Building.builder().id(7L).build();
//        Building building2 = Building.builder().id(8L).build();
//        Favorite favorite1 = Favorite.builder().id(5L).build(); favorite1.setBuilding(building1);
//        Favorite favorite2 = Favorite.builder().id(6L).build(); favorite2.setBuilding(building2);
//
//
//        List<Favorite> favoriteList = List.of(favorite1, favorite2);
//        given(favoriteRepository.findByMember(member))
//                .willReturn(favoriteList);
//
//        List<Building> resultBuildingList = List.of(building1, building2);
//        List<Long> buildingIds = List.of(building1.getId(), building2.getId());
//        given(buildingCustomRepo.findBuildingsByIdIn(buildingIds, cursorDto.getCursorId(), PageRequest.of(0, cursorDto.getSize())))
//                .willReturn(resultBuildingList);
//
//        // when
//        List<Building> buildingList = favoriteService.getBuildingListByMember(member, cursorDto.getCursorId(), PageRequest.of(0, cursorDto.getSize()));
//
//        // then
//        assertEquals(buildingList.size(), 2);
//    }

    @Test
    void deleteFavoriteBuilding_Test() {
        // given
        Member member = createTestMember();
        given(buildingRepository.findBuildingById(any()))
                .willReturn(testBuilding);
        given(favoriteRepository.findByBuildingAndMember(any(), any()))
                .willReturn(Favorite.builder().id(3L).build());

        // when
        Long deletedFavoriteId = favoriteService.deleteFavoriteBuilding(1L, member);

        // then
        assertNotNull(deletedFavoriteId);
    }
}
