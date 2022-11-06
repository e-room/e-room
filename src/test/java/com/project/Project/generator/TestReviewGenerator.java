package com.project.Project.generator;

import com.project.Project.aws.s3.FileService;
import com.project.Project.config.AmazonConfig;
import com.project.Project.config.WebClientConfig;
import com.project.Project.controller.building.dto.BuildingOptionalDto;
import com.project.Project.controller.review.dto.ReviewBaseDto;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResidencePeriodDto;
import com.project.Project.controller.review.dto.ReviewScoreDto;
import com.project.Project.controller.room.dto.RoomBaseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.repository.RepositoryTestConfig;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.service.building.BuildingService;
import com.project.Project.service.fileProcess.ReviewImageProcess;
import com.project.Project.service.member.MemberService;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewGenerator;
import com.project.Project.service.review.ReviewKeywordService;
import com.project.Project.service.review.ReviewService;
import com.project.Project.service.room.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewGenerator.class, ReviewCategoryService.class, ReviewKeywordService.class, MemberService.class, ReviewService.class, RoomService.class, ReviewImageProcess.class, FileService.class, BuildingService.class, BuildingCustomRepository.class}))
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({RepositoryTestConfig.class, WebClientConfig.class, AmazonConfig.class})
@Commit
public class TestReviewGenerator {

    @Autowired
    WebClient kakaoMapWebClient;


    private class ReviewCreateConfigurer {

        private BuildingRepository buildingRepository;
        private BuildingService buildingService;

        public ReviewCreateConfigurer(BuildingRepository buildingRepository, BuildingService buildingService) {
            this.buildingRepository = buildingRepository;
            this.buildingService = buildingService;
        }

        private Building building;
        private BuildingOptionalDto buildingOptionalDto;
        private RoomBaseDto roomBaseDto;
        private ReviewBaseDto reviewBaseDto;
        private ReviewScoreDto reviewScoreDto;
        private ReviewResidencePeriodDto reviewResidencePeriodDto;
        private List<String> advantageKeywordList = new ArrayList<>();
        private List<String> disadvantageKeywordList = new ArrayList<>();
        private String advantageDescription;
        private String disadvantageDescription;
        private List<MultipartFile> reviewImageList = new ArrayList<>();

        public ReviewCreateConfigurer setBuilding(Long buildingId) {
            Building building = this.buildingRepository.findById(buildingId).orElseThrow(() -> new BuildingException(ErrorCode.BUILDING_NOT_FOUND));
            this.building = building;
            return this;
        }

        public ReviewCreateConfigurer setBuildingOptional(Boolean hasElevator, String buildingName) {
            this.buildingOptionalDto = new BuildingOptionalDto(buildingName, hasElevator);
            return this;
        }

        public ReviewCreateConfigurer setRoomBaseDto(Integer lineNumber, Integer roomNumber) {
            this.roomBaseDto = new RoomBaseDto(lineNumber, roomNumber);
            return this;
        }

        public ReviewCreateConfigurer setReviewBaseDto(Boolean isAnonymous) {
            //다른 값들은 전부 랜덤한 값 생성
            this.reviewBaseDto = new ReviewBaseDto();
            return this;
        }

        public ReviewCreateConfigurer setReviewScoreDto() {
            //기본적으로는 random 값
            //enum 값 옵션에 따라 특정 점수 세팅할 수 있게끔
            return this;
        }

        public ReviewRequestDto.ReviewCreateDto build() {
            return ReviewRequestDto.ReviewCreateDto.builder()
                    .address(Address.toAddressDto(this.building.getAddress()))
                    .buildingOptionalDto(this.buildingOptionalDto)
                    .roomBaseDto(this.roomBaseDto)
                    .reviewBaseDto(this.reviewBaseDto)
                    .reviewScoreDto(this.reviewScoreDto)
                    .reviewResidencePeriodDto(this.reviewResidencePeriodDto)
                    .build();
        }


//        Building building = this.buildingRepository.findBuildingById(this.buildingId);
//        BuildingOptionalDto buildingOptionalDto = new BuildingOptionalDto(buildingName, hasElevator);
//            this.buildingService.createBuilding(building.getAddress(), buildingOptionalDto);

    }

    @Test
    public void init() {

    }
}
