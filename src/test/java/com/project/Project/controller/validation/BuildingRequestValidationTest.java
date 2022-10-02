package com.project.Project.controller.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Project.WithMockCustomOAuth2Account;
import com.project.Project.config.auth.SecurityConfig;
import com.project.Project.controller.building.BuildingRestController;
import com.project.Project.controller.building.dto.BuildingRequestDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.embedded.Address;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.validator.BuildingExistValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BuildingRestController.class}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
},includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BuildingExistValidator.class)
})
@AutoConfigureMockMvc
public class BuildingRequestValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BuildingRepository repository;

    private static BuildingRequestDto.BuildingListRequest getBuildingListRequest(Long id) {
        BuildingRequestDto.BuildingListRequest request1 = BuildingRequestDto.BuildingListRequest.builder()
                .buildingId(id)
                .build();
        return request1;
    }
    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void BuildingExistsTest() throws Exception {
        //given
        given(repository.existsById(1L)).willReturn(true);
        given(repository.existsById(2L)).willReturn(true);
        given(repository.existsById(3L)).willReturn(true);

        BuildingRequestDto.BuildingListRequest request1 = getBuildingListRequest(5L);
        BuildingRequestDto.BuildingListRequest request2 = getBuildingListRequest(6L);

        BuildingRequestDto.BuildingListRequest request3 = getBuildingListRequest(1L);
        BuildingRequestDto.BuildingListRequest request4 = getBuildingListRequest(2L);


        String badRequestContent = objectMapper.writeValueAsString(List.of(request1, request2));
        String goodRequestContent = objectMapper.writeValueAsString(List.of(request3, request4));


        //when
        final ResultActions action1 = mockMvc.perform(
                        get("/building/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("buildingId","5,6")
//                                .content(badRequestContent)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andDo(print());

        final ResultActions action2 = mockMvc.perform(
                        get("/building/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("buildingId","1,2")
//                                .content(goodRequestContent)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void BuildingMarkerTest() throws Exception {

        Building testBuilding1 = Building.builder()
                .id(1L).hasElevator(true).address(Address.builder().metropolitanGovernment("대전광역시").basicLocalGovernment("유성구").roadName("대학로").buildingNumber("291").build()).build();
        Building testBuilding2 = Building.builder().id(2L).hasElevator(false).address(Address.builder().metropolitanGovernment("서울 특별시").basicLocalGovernment("관악구").roadName("관천로").buildingNumber("47").build()).build();


        //given
        given(repository.existsById(1L)).willReturn(true);
        given(repository.existsById(2L)).willReturn(true);
        given(repository.existsById(3L)).willReturn(true);

        //when
        final ResultActions actions = mockMvc.perform(
                        get("/building/marking")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void BuildingSearchTest() throws Exception {
        //given
        given(repository.existsById(1L)).willReturn(true);
        given(repository.existsById(2L)).willReturn(true);
        given(repository.existsById(3L)).willReturn(true);

        //when
        final ResultActions action1 = mockMvc.perform(
                        get("/building/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("params","대덕")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print());
    }
}
