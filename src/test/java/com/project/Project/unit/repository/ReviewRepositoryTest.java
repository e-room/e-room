//package com.project.Project.repository;
//
//import com.project.Project.domain.member.Member;
//import com.project.Project.domain.embedded.AnonymousStatus;
//import com.project.Project.domain.review.Review;
//import com.project.Project.domain.room.Room;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ReviewRepositoryTest {
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private RoomRepository roomRepository;
//
//    @Autowired
//    private ReviewFormRepository reviewFormRepository;
//
//    @Test
//    void findByIdInOrderByCreatedAtDescTest() throws Exception {
//        // given
//        List<Long> reviewIdList = new ArrayList<>();
//        Long lastSavedReviewId = null;
//        for(long i=1; i<=10; i++) {
//            Member member = memberRepository.save(Member.builder().build());
//            Room room = roomRepository.save(Room.builder().roomNumber(302).build());
//            ReviewForm reviewForm = reviewFormRepository.save(ReviewForm.builder().build());
//
//            Review review = Review.builder()
//                    .member(member)
//                    .room(room)
//                    .likeMemberList(new ArrayList<>())
//                    .likeCnt(0)
//                    .reviewForm(reviewForm)
//                    .reviewSummaryList(new ArrayList<>())
//                    .anonymousStatus(AnonymousStatus.generateAnonymousStatus())
//                    .build();
//            Review savedReview = reviewRepository.save(review);
//            reviewIdList.add(savedReview.getId());
//        }
//
//        // when
//        List<Review> reviewList = reviewRepository.findByIdInAndIdLessThanOrderByCreatedAtDesc(reviewIdList, reviewIdList.get(9), PageRequest.of(0, 5));
//
//        for(Review review : reviewList) {
//            System.out.println(review.getId() + " " + review.getCreatedAt());
//        }
//        System.out.println(reviewIdList.get(9));
//        // then
//        assertEquals(5, reviewList.size());
//    }
//}
