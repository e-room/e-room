package com.project.Project.controller.interaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Project.auth.filter.CustomBasicAuthFilter;
import com.project.Project.config.SecurityConfig;
import com.project.Project.controller.interaction.controller.FavoriteRestController;
import com.project.Project.domain.Member;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.review.ReviewCategoryRepository;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.service.impl.FavoriteServiceImpl;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewKeywordService;
import com.project.Project.util.annotation.WithMockCustomOAuth2Account;
import com.project.Project.validator.BuildingExistValidator;
import com.project.Project.validator.interaction.FavoriteExistValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FavoriteRestController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, CustomBasicAuthFilter.class})
        },
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewKeywordService.class, ReviewCategoryService.class, BuildingExistValidator.class}))
@ActiveProfiles("local")
@AutoConfigureMockMvc
public class FavoriteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FavoriteServiceImpl favoriteService;

    @MockBean
    private ReviewCategoryRepository reviewCategoryRepository;

    @MockBean
    private ReviewKeywordRepository reviewKeywordRepository;

    @MockBean
    private BuildingRepository buildingRepository;

    @MockBean
    private FavoriteExistValidator favoriteExistValidator;

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void addFavoriteBuilding_Test() throws Exception {
        // given
        given(favoriteService.addFavoriteBuilding(any(), any(Member.class)))
                .willReturn(10L);
        given(buildingRepository.existsById(any()))
                .willReturn(true);

        // when & then
        final ResultActions action1 = mockMvc.perform(
                        post("/member/favorite/3")
                                .with(csrf())
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void deleteFavoriteBuilding_Test() throws Exception {
        // given
        given(favoriteService.deleteFavoriteBuilding(any(), any(Member.class)))
                .willReturn(10L);
        given(favoriteExistValidator.isValid(any(Member.class), any()))
                .willReturn(true);
        given(buildingRepository.existsById(any()))
                .willReturn(true);

        // when & then
        final ResultActions action1 = mockMvc.perform(
                        delete("/member/favorite/10")
                                .with(csrf())
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void ExistFavorite_GOOD_Test() throws Exception {
        // given
        given(favoriteService.deleteFavoriteBuilding(any(), any(Member.class)))
                .willReturn(10L);

        given(favoriteExistValidator.isValid(any(Member.class), any()))
                .willReturn(true);

        given(buildingRepository.existsById(any()))
                .willReturn(true);

        // when & then
        final ResultActions action1 = mockMvc.perform(
                        delete("/member/favorite/3")
                                .with(csrf())
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void ExistFavorite_BAD_Test() throws Exception {
        // given
        given(favoriteService.deleteFavoriteBuilding(any(), any(Member.class)))
                .willReturn(10L);

        given(favoriteExistValidator.isValid(any(Member.class), eq(11L)))
                .willReturn(false);

        given(buildingRepository.existsById(any()))
                .willReturn(true);

        // when & then
        final ResultActions action2 = mockMvc.perform(
                        delete("/member/favorite/11")
                                .with(csrf())
                ).andExpect(status().isNotFound())
                .andDo(print());
    }
}
