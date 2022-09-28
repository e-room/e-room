package com.project.Project.service.impl;

import com.project.Project.domain.enums.FileFolder;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.repository.ReviewImageRepository;
import com.project.Project.service.ReviewImageService;
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
        for(MultipartFile multipartFile : imageFileList) {
            String url = fileProcessService.uploadImage(multipartFile, FileFolder.REVIEW_IMAGES);
            ReviewImage reviewImage = ReviewImage.builder().url(url).build();
                reviewImage.setReview(review);
            reviewImageRepository.save(reviewImage);
        }
    }
}
