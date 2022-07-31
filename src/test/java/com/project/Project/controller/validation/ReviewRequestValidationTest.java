package com.project.Project.controller.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Project.WithMockCustomOAuth2Account;
import com.project.Project.config.auth.SecurityConfig;
import com.project.Project.controller.review.controller.ReviewRestController;
import com.project.Project.repository.BuildingRepository;
import com.project.Project.repository.ReviewRepository;
import com.project.Project.repository.RoomRepository;
import com.project.Project.validator.BuildingExistValidator;
import com.project.Project.validator.ReviewExistValidator;
import com.project.Project.validator.RoomExistValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ReviewRestController.class},
excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
}, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewExistValidator.class, BuildingExistValidator.class, RoomExistValidator.class})
})
@AutoConfigureMockMvc
public class ReviewRequestValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private RoomRepository roomRepository;

    @MockBean
    private BuildingRepository buildingRepository;

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void RoomExistsTest() throws Exception{
        given(roomRepository.existsById(1L)).willReturn(true);
        given(roomRepository.existsById(2L)).willReturn(true);

        final ResultActions existRoomRequest = mockMvc.perform(
                get("/building/room/1/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(print());


        final ResultActions badParameterTypeRequest = mockMvc.perform(
                get("/building/room/a/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andDo(print());



        final ResultActions notExistRoomRequest = mockMvc.perform(
                get("/building/room/3/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void ReviewExistsTest() throws Exception{
        given(reviewRepository.existsById(1L)).willReturn(true);
        given(reviewRepository.existsById(2L)).willReturn(true);

        final ResultActions existRoomRequest = mockMvc.perform(
                delete("/building/room/review/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andDo(print());


        final ResultActions badParameterTypeRequest = mockMvc.perform(
                delete("/building/room/review/a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andDo(print());


        final ResultActions notExistRoomRequest = mockMvc.perform(
                delete("/building/room/review/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andDo(print());
    }
}
