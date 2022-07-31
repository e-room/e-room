package com.project.Project.controller.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Project.WithMockCustomOAuth2Account;
import com.project.Project.config.auth.SecurityConfig;
import com.project.Project.controller.building.BuildingRestController;
import com.project.Project.controller.building.dto.BuildingRequestDto;
import com.project.Project.domain.building.Building;
import com.project.Project.repository.BuildingRepository;
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

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", restrationId = "google")
    void BuildingExistsTest() throws Exception {
        Building testBuilding = Building.builder()
                .id(1L).hasElevator(true).build();

        //given
        given(repository.existsById(1L)).willReturn(true);
        given(repository.existsById(2L)).willReturn(true);
        given(repository.existsById(3L)).willReturn(true);

        BuildingRequestDto.BuildingListRequest request1 = BuildingRequestDto.BuildingListRequest.builder()
                .buildingId(5L)
                .build();

        BuildingRequestDto.BuildingListRequest request2 = BuildingRequestDto.BuildingListRequest.builder()
                .buildingId(6L)
                .build();

        String content = objectMapper.writeValueAsString(List.of(request1, request2));

        //when
        final ResultActions actions = mockMvc.perform(
                        get("/building/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", restrationId = "google")
    void BuildingMarkerTest() throws Exception {
        Building testBuilding = Building.builder()
                .id(1L).hasElevator(true).build();

        //given
        given(repository.existsById(1L)).willReturn(true);
        given(repository.existsById(2L)).willReturn(true);
        given(repository.existsById(3L)).willReturn(true);

        BuildingRequestDto.BuildingListRequest request1 = BuildingRequestDto.BuildingListRequest.builder()
                .buildingId(5L)
                .build();

        BuildingRequestDto.BuildingListRequest request2 = BuildingRequestDto.BuildingListRequest.builder()
                .buildingId(6L)
                .build();

        String content = objectMapper.writeValueAsString(List.of(request1, request2));

        //when
        final ResultActions actions = mockMvc.perform(
                        get("/building/marking")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content)
//                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print());
    }
}
