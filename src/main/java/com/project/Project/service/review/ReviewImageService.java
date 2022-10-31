package com.project.Project.service.review;

import com.project.Project.domain.review.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewImageService {
    void saveImageList(List<MultipartFile> imageFileList, Review review);
}
