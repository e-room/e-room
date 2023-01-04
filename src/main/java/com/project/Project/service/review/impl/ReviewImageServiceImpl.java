package com.project.Project.service.review.impl;

import com.project.Project.aws.s3.ReviewImagePackageMetaMeta;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.review.ReviewImageRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.fileProcess.ReviewImageProcess;
import com.project.Project.service.review.ReviewImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService {

    private final ReviewImageProcess reviewImageProcess;

    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;

    public List<ReviewImage> findByReview(Long reviewId) {
        // controller 단에서 존재하는 @ExistReview로 검증하여 존재하는 reviewId이므로 바로 get
        Review review = reviewRepository.findById(reviewId).get();
        return reviewImageRepository.findByReview(review);
    }

    public ReviewImage findByUuid(String uuid) {
        // controller 단에서 존재하는 @ExistReviewImage로 검증하여 존재하는 uuid이므로 바로 get
        ReviewImage reviewImage = reviewImageRepository.findByUuid(uuid).get();
        return reviewImage;
    }

    @Transactional
    public void saveImageList(List<MultipartFile> imageFileList, Review review) {
        Room room = review.getRoom();
        Building building = room.getBuilding();
        String uuid = "asdfa";
        ReviewImagePackageMetaMeta reviewImagePackageMeta = ReviewImagePackageMetaMeta.builder()
                .buildingId(building.getId())
                .roomId(room.getId())
                .uuid(uuid)
                .build();
        for (MultipartFile multipartFile : imageFileList) {
            String url = reviewImageProcess.uploadImage(multipartFile, reviewImagePackageMeta);
            ReviewImage reviewImage = ReviewImage.builder().url(url).build();
            reviewImage.setReview(review);
            reviewImageRepository.save(reviewImage);
        }
    }
}
