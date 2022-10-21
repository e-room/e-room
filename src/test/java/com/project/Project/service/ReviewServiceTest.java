package com.project.Project.service;

import com.project.Project.domain.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

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
