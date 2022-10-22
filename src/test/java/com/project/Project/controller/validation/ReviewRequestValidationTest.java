//package com.project.Project.controller.validation;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.Project.WithMockCustomOAuth2Account;
//import com.project.Project.config.auth.SecurityConfig;
//import com.project.Project.controller.review.controller.ReviewRestController;
//import com.project.Project.repository.building.BuildingRepository;
//import com.project.Project.repository.review.ReviewRepository;
//import com.project.Project.repository.room.RoomRepository;
//import com.project.Project.service.BuildingService;
//import com.project.Project.service.ReviewService;
//import com.project.Project.service.RoomService;
//import com.project.Project.validator.BuildingExistValidator;
//import com.project.Project.validator.EnumValidator;
//import com.project.Project.validator.ReviewExistValidator;
//import com.project.Project.validator.RoomExistValidator;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import javax.xml.transform.Result;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = {ReviewRestController.class},
//excludeFilters = {
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
//}, includeFilters = {
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ReviewExistValidator.class, BuildingExistValidator.class, RoomExistValidator.class, EnumValidator.class})
//})
//@AutoConfigureMockMvc
//public class ReviewRequestValidationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ReviewRepository reviewRepository;
//
//    @MockBean
//    private ReviewService reviewService;
//
//    @MockBean
//    private BuildingService buildingService;
//
//    @MockBean
//    private RoomService roomService;
//
//    @MockBean
//    private RoomRepository roomRepository;
//
//    @MockBean
//    private BuildingRepository buildingRepository;
//    private final String pathForGoodExample = "src/test/java/com/project/Project/controller/validation/sample/good";
//    private final String pathForBadExample = "src/test/java/com/project/Project/controller/validation/sample/bad";
//    private Integer goodExampleFileCnt;
//    private Integer badExampleFileCnt;
//    @BeforeEach
//    void setup(){
//        File gf = new File(pathForGoodExample);
//        goodExampleFileCnt = 0;
//        for (File file : gf.listFiles()) {
//            if(file.isFile() && file.getName().endsWith(".json")) goodExampleFileCnt++;
//        }
//
//
//        File bf = new File(pathForBadExample);
//        badExampleFileCnt = 0;
//        for (File file : bf.listFiles()) {
//            if(file.isFile() && file.getName().endsWith(".json")) badExampleFileCnt++;
//        }
//    }
//    @Test
//    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
//    void RoomExistsTest() throws Exception{
//        given(roomRepository.existsById(1L)).willReturn(true);
//        given(roomRepository.existsById(2L)).willReturn(true);
//
//        final ResultActions existRoomRequest = mockMvc.perform(
//                get("/building/room/1/review")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk()).andDo(print());
//
//
//        final ResultActions badParameterTypeRequest = mockMvc.perform(
//                get("/building/room/a/review")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isBadRequest()).andDo(print());
//
//
//
//        final ResultActions notExistRoomRequest = mockMvc.perform(
//                get("/building/room/3/review")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isBadRequest()).andDo(print());
//    }
//
//    @Test
//    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
//    void ReviewCreateDtoTest() throws Exception {
//        File gf = new File(pathForGoodExample);
//        for (File file : gf.listFiles()) {
//            if(file.isFile() && file.getName().endsWith(".json")){
//                try{
//                    Path path = Paths.get(file.getPath());
//                    Stream<String> lines = Files.lines(path);
//                    String content = lines.collect(Collectors.joining(System.lineSeparator()));
//
//                    final ResultActions goodResult = mockMvc.perform(
//                            post("/building/room/review")
//                                    .contentType(MediaType.APPLICATION_JSON)
//                                    .accept(MediaType.APPLICATION_JSON)
//                                    .with(csrf())
//                                    .content(content)
//                    ).andExpect(status().isOk()).andDo(print());
//                    lines.close();
//                }catch (Exception e){
//                    e.printStackTrace();
//                    throw e;
//                }
//            }
//        }
//
//        File bf = new File(pathForBadExample);
//        for (File file : bf.listFiles()) {
//            if(file.isFile() && file.getName().endsWith(".json")){
//                try{
//                    Path path = Paths.get(file.getPath());
//                    Stream<String> lines = Files.lines(path);
//                    String content = lines.collect(Collectors.joining(System.lineSeparator()));
//
//                    final ResultActions badResult = mockMvc.perform(
//                            post("/building/room/review")
//                                    .contentType(MediaType.APPLICATION_JSON)
//                                    .accept(MediaType.APPLICATION_JSON)
//                                    .with(csrf())
//                                    .content(content)
//                    ).andExpect(status().isBadRequest())
//                            .andDo((result)-> {
//                                String message = String.format("testCase: %s \n\tstatus: %d \n\tresponse body: %s \n",file.getName(), result.getResponse().getStatus(), result.getResponse().getContentAsString());
//                                System.out.println(message);
//                            });
//                    lines.close();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    @Test
//    @WithMockCustomOAuth2Account(role = "ROLE_USER", registrationId = "google")
//    void ReviewExistsTest() throws Exception{
//        given(reviewRepository.existsById(1L)).willReturn(true);
//        given(reviewRepository.existsById(2L)).willReturn(true);
//
//        final ResultActions existRoomRequest = mockMvc.perform(
//                delete("/building/room/review/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(csrf())
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk()).andDo(print());
//
//
//        final ResultActions badParameterTypeRequest = mockMvc.perform(
//                delete("/building/room/review/a")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(csrf())
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isBadRequest()).andDo(print());
//
//
//        final ResultActions notExistRoomRequest = mockMvc.perform(
//                delete("/building/room/review/3")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(csrf())
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isBadRequest()).andDo(print());
//    }
//}
