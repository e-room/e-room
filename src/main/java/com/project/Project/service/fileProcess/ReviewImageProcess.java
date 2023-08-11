package com.project.Project.service.fileProcess;

import com.project.Project.common.aws.s3.FileService;
import com.project.Project.common.aws.s3.ReviewImagePackageMetaMeta;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.review.ReviewException;
import com.project.Project.repository.uuid.UuidCustomRepositoryImpl;
import com.project.Project.repository.uuid.UuidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ReviewImageProcess extends FileProcessServiceImpl<ReviewImagePackageMetaMeta> {

    @Autowired
    public ReviewImageProcess(FileService amazonS3Service, UuidCustomRepositoryImpl uuidCustomRepository, UuidRepository uuidRepository) {
        super(amazonS3Service, uuidCustomRepository, uuidRepository);
    }

    /*
    @todo
    save Image asynchronously
    ref: https://www.code4copy.com/java/spring-boot-upload-s3/
     */
    //setImage to entity
    public Review uploadImageAndMapToReview(MultipartFile file, ReviewImagePackageMetaMeta reviewImagePackageMeta, Review review) {
        Optional.ofNullable(review).orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
        String url = this.uploadImage(file, reviewImagePackageMeta);
        ReviewImage reviewImage = ReviewImage.builder().url(url)
                .uuid(reviewImagePackageMeta.getUuidEntity())
                .fileName(file.getOriginalFilename()).build();
        reviewImage.setReview(review);
        return review;
    }
}
