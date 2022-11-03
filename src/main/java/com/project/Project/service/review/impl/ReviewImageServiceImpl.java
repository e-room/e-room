package com.project.Project.service.review.impl;

import com.project.Project.aws.s3.ReviewImagePackageMetaMeta;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.review.ReviewImageRepository;
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

    @Transactional
    public void saveImageList(List<MultipartFile> imageFileList, Review review) {
        Room room = review.getRoom();
        Building building = room.getBuilding();
        ReviewImagePackageMetaMeta reviewImagePackageMeta = ReviewImagePackageMetaMeta.builder()
                .buildingId(building.getId())
                .roomId(room.getId())
                .build();
        for (MultipartFile multipartFile : imageFileList) {
            String url = reviewImageProcess.uploadImage(multipartFile, reviewImagePackageMeta);
            ReviewImage reviewImage = ReviewImage.builder().url(url).build();
            reviewImage.setReview(review);
            reviewImageRepository.save(reviewImage);
        }
    }
}
