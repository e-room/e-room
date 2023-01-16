package com.project.Project.controller.file;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Project.auth.filter.CustomBasicAuthFilter;
import com.project.Project.config.SecurityConfig;
import com.project.Project.config.WebConfig;
import com.project.Project.config.properties.CorsProperties;
import com.project.Project.controller.review.controller.ReviewRestController;
import com.project.Project.domain.Uuid;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.repository.review.ReviewCategoryRepository;
import com.project.Project.repository.review.ReviewImageRepository;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.service.ThumbnailImageService;
import com.project.Project.service.fileProcess.ThumbnailImageProcess;
import com.project.Project.service.review.ReviewCategoryService;
import com.project.Project.service.review.ReviewImageService;
import com.project.Project.service.review.ReviewKeywordService;
import com.project.Project.service.review.impl.ReviewImageServiceImpl;
import com.project.Project.util.annotation.WithMockCustomOAuth2Account;
import com.project.Project.validator.ReviewExistValidator;
import com.project.Project.validator.ReviewImageExistValidator;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ImageRestController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, CustomBasicAuthFilter.class, CorsProperties.class, WebConfig.class})
        },
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewKeywordService.class, ReviewCategoryService.class, ReviewImageExistValidator.class}))
@ActiveProfiles("local")
@AutoConfigureMockMvc
public class ImageRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewCategoryRepository reviewCategoryRepository;

    @MockBean
    private ReviewKeywordRepository reviewKeywordRepository;

    @MockBean
    private ReviewImageRepository reviewImageRepository;

    @MockBean
    private ThumbnailImageService thumbnailImageService;

    @MockBean
    private ReviewImageServiceImpl reviewImageService;

    Review review = Review.builder()
            .id(1L)
            .build();

    ReviewImage reviewImage1 = ReviewImage.builder()
            .review(review)
            .id(2L)
            .uuid(Uuid.builder().uuid("iloveeroom").build())
            .url("https://d2ykyi5jl9muoc.cloudfront.net/profile-images/blue-smile_eyes-d_mouth.png")
            .build();


    @DisplayName("이미지 리스트 반환 by uuid TEST")
    @Test
    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
    void getReviewImageByUuid_TEST() throws Exception {
        // given
        given(reviewImageRepository.existsByUuid("iloveeroom"))
                .willReturn(true);
        given(reviewImageRepository.existsByUuid("iloveeroom2"))
                .willReturn(false);
        given(reviewImageService.findByUuid("iloveeroom"))
                .willReturn(reviewImage1);

        // when & then
        final ResultActions action1 = mockMvc.perform(
                        get("/image?uuid=iloveeroom")
                                .with(csrf())
                ).andExpect(status().isOk())
                .andDo(print());

        final ResultActions action2 = mockMvc.perform(
                        get("/image?uuid=iloveeroom2")
                                .with(csrf())
                ).andExpect(status().isBadRequest())
                .andDo(print());
    }
}
