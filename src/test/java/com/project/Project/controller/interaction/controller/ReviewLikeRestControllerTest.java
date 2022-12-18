package com.project.Project.controller.interaction.controller;

import com.project.Project.auth.filter.CustomBasicAuthFilter;
import com.project.Project.config.SecurityConfig;
import com.project.Project.domain.Member;
import com.project.Project.repository.review.ReviewCategoryRepository;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.interaction.impl.ReviewLikeServiceImpl;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewKeywordService;
import com.project.Project.util.annotation.WithMockCustomOAuth2Account;
import com.project.Project.validator.ReviewExistValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ReviewLikeRestController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, CustomBasicAuthFilter.class})
        },
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewKeywordService.class, ReviewCategoryService.class, ReviewExistValidator.class}))
@ActiveProfiles("local")
@AutoConfigureMockMvc
public class ReviewLikeRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewLikeServiceImpl reviewLikeService;

    @MockBean
    private ReviewCategoryRepository reviewCategoryRepository;

    @MockBean
    private ReviewKeywordRepository reviewKeywordRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void updateReviewLike_Test() throws Exception {
        // given
        given(reviewLikeService.updateReviewLike(any(), any(Member.class)))
                .willReturn(15L);
        given(reviewRepository.existsById(eq(3L)))
                .willReturn(true);

        // when & then
        mockMvc.perform(
                        post("/building/room/review/like/3").with(csrf())
                ).andExpect(status().isOk())
                .andDo(print());

    }
}