package com.project.Project.service;

import com.project.Project.domain.building.Building;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingCustomRepositoryImpl;
import com.project.Project.service.building.BuildingService;
import com.project.Project.service.building.impl.BuildingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@SpringBootTest
public class BuildingServiceImplTest {
    private final BuildingService buildingService;
    private final BuildingCustomRepository buildingRepository;

    private String newAddress = "애월로1길 24-9";
    private String badAddress = "이상한로 1234";
    private int userCount = 2;

    @Autowired
    public BuildingServiceImplTest(BuildingService buildingService, BuildingCustomRepository buildingRepository) {
        this.buildingService = buildingService;
        this.buildingRepository = buildingRepository;
    }

    @Test
    public void testConcurrentBuildingSearch() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(userCount); // 2개의 스레드를 생성
        CountDownLatch latch = new CountDownLatch(userCount); // 2개의 작업이 완료될 때까지 기다리는 래치를 생성

        for (int i = 0; i < userCount; i++) {
            executorService.submit(() -> {
                try {
                    buildingService.getBuildingsBySearch(newAddress, new ArrayList<>(), PageRequest.of(0, 4, Sort.by("id", "DESC"))); // 검색 메소드를 호출
                } finally {
                    latch.countDown(); // 작업이 완료되면 래치의 카운트를 감소
                }
            });
        }

        latch.await(); // 모든 작업이 완료될 때까지 대기

        // 이후에 디비 상태를 체크
        List<Building> buildingList = buildingRepository.searchBuildings(newAddress, new ArrayList<>(), PageRequest.of(0, 4, Sort.by("id", "DESC")));
        assertNotEquals(userCount, buildingList.size());
        assertEquals(1, buildingList.size()); // 하나의 데이터만 리턴되어야 함.
    }

    @Test
    public void testConcurrentBadBuildingSearch() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(userCount); // 2개의 스레드를 생성
        CountDownLatch latch = new CountDownLatch(userCount); // 2개의 작업이 완료될 때까지 기다리는 래치를 생성

        for (int i = 0; i < userCount; i++) {
            executorService.submit(() -> {
                try {
                    buildingService.getBuildingsBySearch(badAddress, new ArrayList<>(), PageRequest.of(0, 4, Sort.by("id", "DESC"))); // 검색 메소드를 호출
                } finally {
                    latch.countDown(); // 작업이 완료되면 래치의 카운트를 감소
                }
            });
        }

        latch.await(); // 모든 작업이 완료될 때까지 대기

        // 이후에 디비 상태를 체크
        List<Building> buildingList = buildingRepository.searchBuildings(badAddress, new ArrayList<>(), PageRequest.of(0, 4, Sort.by("id", "DESC")));
        assertEquals(0, buildingList.size()); // 빈 리스트가 넘어와야 함.
    }
}
