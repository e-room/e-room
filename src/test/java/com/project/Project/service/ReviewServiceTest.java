package com.project.Project.service;

import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.ReviewRepository;
import com.project.Project.repository.RoomRepository;
import com.project.Project.serializer.review.ReviewSerializer;
import com.project.Project.service.impl.ReviewServiceImpl;
import com.project.Project.service.impl.RoomServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    public void save_Test() {
        // given
        Review review = Review.builder()
                .id(1L)
                .room(Room.builder().build())
                .member(Member.builder().build())
                .build();
        given(reviewRepository.save(any()))
                .willReturn(review);


        // when
        Long savedReviewId = reviewService.save(review);

        // then
        assertEquals(review.getId(), savedReviewId);
    }

    public void delete_Test() {

    }
}
