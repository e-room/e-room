package com.project.Project.service.review.impl;

import com.project.Project.aws.s3.ReviewImagePackageMetaMeta;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import com.project.Project.exception.review.ReviewException;
import com.project.Project.exception.review.ReviewImageException;
import com.project.Project.repository.building.BuildingRepository;
import com.project.Project.repository.review.ReviewImageRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.fileProcess.ReviewImageProcess;
import com.project.Project.service.review.ReviewImageService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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
    private final BuildingRepository buildingRepository;
    private final ReviewRepository reviewRepository;

    public List<ReviewImage> findByReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
        return reviewImageRepository.findByReview(review);
    }

    public ReviewImage findByUuid(String uuid) {
        return reviewImageRepository.findByUuid(uuid).orElseThrow(() -> new ReviewImageException(ErrorCode.REVIEW_IMAGE_NOT_FOUND));;
    }

    @Transactional
    public void saveImageList(List<MultipartFile> imageFileList, Review review) {
        Building building = review.getBuilding();
        String uuid = "asdfa";
        ReviewImagePackageMetaMeta reviewImagePackageMeta = ReviewImagePackageMetaMeta.builder()
                .buildingId(building.getId())
                .uuid(uuid)
                .build();
        for (MultipartFile multipartFile : imageFileList) {
            String url = reviewImageProcess.uploadImage(multipartFile, reviewImagePackageMeta);
            ReviewImage reviewImage = ReviewImage.builder().url(url).build();
            reviewImage.setReview(review);
            reviewImageRepository.save(reviewImage);
        }
    }

    @Override
    public List<ReviewImage> findByBuilding(Long buildingId) {
        Building building = buildingRepository.findById(buildingId).orElseThrow(() -> new BuildingException(ErrorCode.BUILDING_NOT_FOUND));
        List<ReviewImage> reviewImageList = reviewImageRepository.findByBuilding(building);
        reviewImageList.stream().map(ReviewImage::getReview).forEach(Hibernate::initialize);
        return reviewImageList;
    }

}
