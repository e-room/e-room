package com.project.Project.unit.controller.review.controller;


import com.project.Project.config.SecurityConfig;
import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.review.controller.ReviewRestController;
import com.project.Project.controller.review.dto.ReviewBaseDto;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResidencePeriodDto;
import com.project.Project.controller.review.dto.ReviewScoreDto;
import com.project.Project.domain.building.Building;
import com.project.Project.service.building.impl.BuildingServiceImpl;
import com.project.Project.service.review.impl.ReviewImageServiceImpl;
import com.project.Project.service.review.impl.ReviewServiceImpl;
import com.project.Project.util.annotation.WithMockCustomOAuth2Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ReviewRestController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
@AutoConfigureMockMvc
public class ReviewRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewServiceImpl reviewService;
    @MockBean
    private BuildingServiceImpl buildingService;


    @MockBean
    private ReviewImageServiceImpl reviewImageService;
    // given
    String advantageDescription = "길지 풍부하게 보이는 곳이 밝은 있으랴? 싹이 있으며, 따뜻한 바이며, 일월과 청춘 때문이다. 스며들어 사랑의 끓는 무엇을 부패를 말이다. 자신과 품으며, 이상이 같지 천지는 위하여 인생에 속잎나고, 바이며, 말이다. 속잎나고, 그들의 피부가 영원히 얼마나 이것을 그리하였는가? 인간이 용감하고 영락과 품고 전인 있으랴? 실현에 심장의 동산에는 얼음에 기쁘며, 사는가 위하여서. 것이 스며들어 그들에게 불어 보내는 크고 인간의 품고 이 말이다. 품으며, 하는 찬미를 봄바람을 피가 하는 그들을 그러므로 봄바람이다. 그들에게 목숨을 가슴이 무엇을 행복스럽고 것이다.";
    String disadvantageDescription = "길지 풍부하게 보이는 곳이 밝은 있으랴? 싹이 있으며, 따뜻한 바이며, 일월과 청춘 때문이다. 스며들어 사랑의 끓는 무엇을 부패를 말이다. 자신과 품으며, 이상이 같지 천지는 위하여 인생에 속잎나고, 바이며, 말이다. 속잎나고, 그들의 피부가 영원히 얼마나 이것을 그리하였는가? 인간이 용감하고 영락과 품고 전인 있으랴? 실현에 심장의 동산에는 얼음에 기쁘며, 사는가 위하여서. 것이 스며들어 그들에게 불어 보내는 크고 인간의 품고 이 말이다. 품으며, 하는 찬미를 봄바람을 피가 하는 그들을 그러므로 봄바람이다. 그들에게 목숨을 가슴이 무엇을 행복스럽고 것이다.";

    ReviewRequestDto.ReviewCreateDto reviewCreateDto = ReviewRequestDto.ReviewCreateDto.builder()
            .address(
                    AddressDto.builder()
                            .siDo("경기도")
                            .siGunGu("수원시 영통구")
                            .roadName("매영로425번길")
                            .eupMyeon("")
                            .buildingNumber("4")
                            .build()
            )
            .reviewResidencePeriodDto(ReviewResidencePeriodDto.builder().residenceStartYear(2020).residenceDuration(12).build())
            .reviewBaseDto(ReviewBaseDto.builder().deposit(800).monthlyRent(50).managementFee(10).netLeasableArea(13.0).build())
            .reviewScoreDto(ReviewScoreDto.builder().traffic(3.0).buildingComplex(4.0).surrounding(1.0).internal(5.0).livingLocation(3.0)
                    .residenceSatisfaction(5.0).build())
            .advantageKeywordList(List.of("PARKING", "VENTILATION", "TOWN_NOISE"))
            .advantageDescription(advantageDescription)
            .disadvantageKeywordList(List.of("MART_CONVENIENCE_STORE", "DAY_LIGHTING"))
            .disadvantageDescription(disadvantageDescription)
            .reviewImageList(new ArrayList<>())
            .build();

    // https://jhkimmm.tistory.com/31
    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    public void createReview_Test() throws Exception {
        // given
        Building building = Building.builder().build();
        given(buildingService.findByAddress(any()))
                .willReturn(Optional.ofNullable(building));
        // reviewSerializer를 모킹, 관련 의존성 모킹 후 돌려보기


        final String fileName = "test"; //파일명
        final String contentType = "png"; //파일타입


        final String filePath = "src/main/resources/static/" + fileName + "." + contentType; //파일경로
        FileInputStream fileInputStream = new FileInputStream(filePath);

        //Mock파일생성
        MockMultipartFile image1 = new MockMultipartFile(
                "reviewImageList", //name
                fileName + "." + contentType, //originalFilename
                contentType,
                fileInputStream
        );


//        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
//        Field[] fields = reviewCreateDto.getClass().getDeclaredFields();
//        for(Field field : fields) {
//            field.setAccessible(true);
//            if(field.getName() == "reviewImageList") continue;
//            multiValueMap.add(field.getName(), String.valueOf(field.get(reviewCreateDto)));
//        }

        // when & then
        MvcResult mvcResult = mockMvc.perform(
                        multipart("/building/room/review")
                                .file(image1)
                                .param("address", "경기도 수원시 영통구 매영로425번길 4(영통동)")
                                .param("lineNumber", "101")
                                .param("roomNumber", "103")
                                .param("residenceType", "APARTMENT")
                                .param("residencePeriod", "UNTIL_NINETEEN")
                                .param("floorHeight", "LOW")
                                .param("deposit", "800")
                                .param("monthlyRent", "50")
                                .param("managementFee", "10")
                                .param("netLeasableArea", "13.0")
                                .param("traffic", "3.0")
                                .param("buildingComplex", "4.0")
                                .param("surrounding", "1.0")
                                .param("internal", "5.0")
                                .param("livingLocation", "3.0")
                                .param("advantageKeywordList", "PARKING,VENTILATION,TOWN_NOISE")
                                .param("advantageDescription", advantageDescription)
                                .param("disadvantageKeywordList", "MART_CONVENIENCE_STORE,DAY_LIGHTING")
                                .param("disadvantageDescription", disadvantageDescription)
                                .param("residenceSatisfaction", "5.0")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);

    }
}
