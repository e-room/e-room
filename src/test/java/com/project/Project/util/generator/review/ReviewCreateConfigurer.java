package com.project.Project.util.generator.review;

import com.project.Project.controller.building.dto.BuildingOptionalDto;
import com.project.Project.controller.review.dto.ReviewBaseDto;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResidencePeriodDto;
import com.project.Project.controller.review.dto.ReviewScoreDto;
import com.project.Project.controller.room.dto.RoomBaseDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.service.building.BuildingService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ReviewCreateConfigurer {

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

    public ReviewCreateConfigurer setBasics(Long buildingId) throws IOException {
        setBuilding(buildingId)
                .setBuildingOptionalBasic()
                .setRoomBaseBasic()
                .setReviewBaseDtoBasic()
                .setReviewScoreBasic()
                .setReviewResidencePeriodBasic()
                .setReviewKeywordBasic()
                .setAdvantageDescription("good" + (int) (Math.random() * 100))
                .setDisadvantageDescription("bad" + (int) (Math.random() * 100))
                .setReviewImageListBasic();
        return this;
    }

    public ReviewCreateConfigurer setBuilding(Long buildingId) {
        Building building = this.buildingRepository.findById(buildingId).orElseThrow(() -> new BuildingException(ErrorCode.BUILDING_NOT_FOUND));
        this.building = building;
        return this;
    }

    public ReviewCreateConfigurer setBuildingOptional(Boolean hasElevator, String buildingName) {
        this.buildingOptionalDto = new BuildingOptionalDto(buildingName, hasElevator);
        return this;
    }

    public ReviewCreateConfigurer setBuildingOptionalBasic() {
        Random random = new Random();
        this.buildingOptionalDto = BuildingOptionalDto.builder()
                .buildingName("test" + random.nextInt())
                .hasElevator(random.nextBoolean())
                .build();
        return this;
    }

    public ReviewCreateConfigurer setRoomBaseDto(Integer lineNumber, Integer roomNumber) {
        this.roomBaseDto = new RoomBaseDto(lineNumber, roomNumber);
        return this;
    }

    private ReviewCreateConfigurer setRoomBaseBasic() {
        this.roomBaseDto = RoomBaseDto.builder()
                .lineNumber((int) Math.random() * 100 + 100)
                .roomNumber((int) Math.random() * 400 + 100)
                .build();
        return this;
    }

    public ReviewCreateConfigurer setReviewBaseDto(Boolean isAnonymous) {
        //다른 값들은 전부 랜덤한 값 생성
        Random random = new Random();
        Integer deposit = random.nextInt(1000) + 100;
        Integer monthlyRent = random.nextInt(40) + 20;
        Integer managementFee = random.nextInt(10) + 3;
        Double netLeasableArea = random.nextDouble();


        this.reviewBaseDto = ReviewBaseDto
                .builder()
                .deposit(deposit)
                .monthlyRent(monthlyRent)
                .managementFee(managementFee)
                .netLeasableArea(netLeasableArea * 100)
                .isAnonymous(isAnonymous)
                .build();
        return this;
    }

    private ReviewCreateConfigurer setReviewBaseDtoBasic() {
        Random random = new Random();
        Boolean isAnonymous = random.nextBoolean();
        setReviewBaseDto(isAnonymous);
        return this;
    }

    public ReviewCreateConfigurer setReviewScoreDto(Double score, ReviewCategoryEnum type) throws IllegalAccessException {
        //기본적으로는 random 값
        //enum 값 옵션에 따라 특정 점수 세팅할 수 있게끔
        setReviewScoreBasic();
        String fieldName = type.name().toLowerCase(Locale.ROOT);
        Field targetField = Arrays.stream(this.reviewScoreDto.getClass().getDeclaredFields()).filter(field -> field.getName().equalsIgnoreCase(fieldName)).findFirst()
                .get();
        targetField.setAccessible(true);
        targetField.setDouble(this.reviewScoreDto, score);
        return this;
    }

    private ReviewCreateConfigurer setReviewScoreBasic() {
        Random random = new Random();
        this.reviewScoreDto = ReviewScoreDto.builder()
                .traffic(Math.random() * 5)
                .buildingComplex(Math.random() * 5)
                .surrounding(Math.random() * 5)
                .internal(Math.random() * 5)
                .livingLocation(Math.random() * 5)
                .residenceSatisfaction(Math.random() * 5)
                .build();
        return this;
    }

    private ReviewCreateConfigurer setReviewResidencePeriodBasic() {
        List<Integer> yearCandidate = List.of(2017, 2018, 2019, 2020, 2021, 2022);
        List<Integer> durationCandidate = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18);
        this.reviewResidencePeriodDto = ReviewResidencePeriodDto.builder()
                .residenceStartYear(yearCandidate.get((int) (Math.random() * 6)))
                .residenceDuration(durationCandidate.get((int) (Math.random() * 18)))
                .build();
        return this;
    }

    public ReviewCreateConfigurer setReviewResidencePeriod(Integer residenceStartYear, Integer residenceDuration) {
        this.reviewResidencePeriodDto = ReviewResidencePeriodDto.builder()
                .residenceStartYear(residenceStartYear)
                .residenceDuration(residenceDuration)
                .build();
        return this;
    }

    private ReviewCreateConfigurer setReviewKeywordBasic() {
        Random random = new Random();
        List<KeywordEnum> advantageList = new ArrayList<>();
        List<KeywordEnum> disadvantageList = new ArrayList<>();
        for (KeywordEnum keyword : KeywordEnum.values()) {
            if (random.nextBoolean()) {
                advantageList.add(keyword);
            }
        }
        for (KeywordEnum keyword : KeywordEnum.values()) {
            if (random.nextBoolean()) {
                disadvantageList.add(keyword);
            }
        }
        this.advantageKeywordList = advantageList.stream().map(Enum::toString).collect(Collectors.toList());
        this.disadvantageKeywordList = disadvantageList.stream().map(Enum::toString).collect(Collectors.toList());
        return this;
    }

    public ReviewCreateConfigurer setAdvantageDescription(String description) {
        this.advantageDescription = description;
        return this;
    }

    public ReviewCreateConfigurer setDisadvantageDescription(String description) {
        this.disadvantageDescription = description;
        return this;
    }

    public ReviewCreateConfigurer setReviewImageList(List<String> fileNames) throws Exception {
        if (fileNames == null || fileNames.isEmpty()) {
            this.reviewImageList = null;
            return this;
        }
        final String contentType = "png"; //파일타입
        List<MultipartFile> images = fileNames.stream().map(fileName -> {
            final String filePath = "./testImage/" + fileName + "." + contentType; //파일경로
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            try {
                MockMultipartFile image1 = new MockMultipartFile(
                        fileName, //name
                        fileName + "." + contentType, //originalFilename
                        contentType,
                        fileInputStream
                );
                return image1;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        this.reviewImageList = images;
        return this;
    }

    private ReviewCreateConfigurer setReviewImageListBasic() throws IOException {
        this.reviewImageList = new ArrayList<>();
        File gf = new File("src/test/java/com/project/Project/generator/review/testImage/");
        final String contentType = "png"; //파일타입
        for (File file : gf.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".png")) {
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                MockMultipartFile image1 = new MockMultipartFile(
                        file.getName(), //name
                        file.getName(), //originalFilename
                        contentType,
                        fileInputStream
                );
                this.reviewImageList.add(image1);
            }
        }
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
                .advantageKeywordList(this.advantageKeywordList)
                .disadvantageKeywordList(this.disadvantageKeywordList)
                .advantageDescription(this.advantageDescription)
                .disadvantageDescription(this.disadvantageDescription)
                .reviewImageList(this.reviewImageList)
                .build();
    }
}