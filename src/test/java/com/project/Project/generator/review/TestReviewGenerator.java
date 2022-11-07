package com.project.Project.generator.review;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Project.aws.s3.FileService;
import com.project.Project.config.AmazonConfig;
import com.project.Project.config.WebClientConfig;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.RepositoryTestConfig;
import com.project.Project.repository.building.BuildingCustomRepository;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.member.MemberRepository;
import com.project.Project.service.building.BuildingService;
import com.project.Project.service.fileProcess.ReviewImageProcess;
import com.project.Project.service.member.MemberService;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewGenerator;
import com.project.Project.service.review.ReviewKeywordService;
import com.project.Project.service.review.ReviewService;
import com.project.Project.service.room.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewGenerator.class, ReviewCategoryService.class, ReviewKeywordService.class, MemberService.class, ReviewService.class, RoomService.class, ReviewImageProcess.class, FileService.class, BuildingService.class, BuildingCustomRepository.class}))
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({RepositoryTestConfig.class, WebClientConfig.class, AmazonConfig.class})
@Commit
public class TestReviewGenerator {

    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    BuildingService buildingService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    private ReviewCreateConfigurer configurer;


    @BeforeEach
    public void setup() {
        this.configurer = new ReviewCreateConfigurer(this.buildingRepository, this.buildingService);
    }

    private Review saveReview(Long targetBuildingId, Long targetMemberId) throws IOException {
        Member member = this.memberService.findById(targetMemberId).orElseGet(() -> this.memberRepository.findAll().get(0));
        ReviewRequestDto.ReviewCreateDto request = this.configurer.setBasics(targetBuildingId).build();
        return this.reviewService.saveReview(request, member);
    }

    @Test
    public void createReviews() throws IOException {
        File file = new File(Paths.get("./testCase/case.json").toUri());
        ObjectMapper objectMapper = new ObjectMapper();
        List<Review> reviewList = new ArrayList<>();

        // JSON File -> Student Object
        List<TestCase> testCaseList = objectMapper.readValue(file, new TypeReference<List<TestCase>>() {
        });
        testCaseList.forEach(x -> System.out.println(x.toString()));
        testCaseList.stream().forEach(testCase -> {
            List<Long> buildingIds = testCase.getBuildingIds().stream().mapToLong((buildingId) -> buildingId.getId()).boxed().collect(Collectors.toList());
            for (Long buildingId : buildingIds) {
                try {
                    reviewList.add(saveReview(buildingId, testCase.getMemberId()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
