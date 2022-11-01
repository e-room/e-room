package com.project.Project.service.review.impl;

import com.project.Project.aws.s3.ReviewImagePackage;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.domain.room.Room;
import com.project.Project.repository.review.ReviewImageRepository;
import com.project.Project.service.FileProcessService;
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

    private final FileProcessService fileProcessService;
    private final ReviewImageRepository reviewImageRepository;

    @Transactional
    public void saveImageList(List<MultipartFile> imageFileList, Review review) {
        Room room = review.getRoom();
        Building building = room.getBuilding();
        ReviewImagePackage reviewImagePackage = ReviewImagePackage.builder()
                .buildingId(building.getId())
                .roomId(room.getId())
                .build();
        for (MultipartFile multipartFile : imageFileList) {
            String url = fileProcessService.uploadImage(multipartFile, reviewImagePackage);
            ReviewImage reviewImage = ReviewImage.builder().url(url).build();
            reviewImage.setReview(review);
            reviewImageRepository.save(reviewImage);
        }
    }
}
