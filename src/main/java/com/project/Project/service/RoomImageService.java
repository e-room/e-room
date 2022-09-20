package com.project.Project.service;

import com.project.Project.domain.review.ReviewForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomImageService {
    void saveImageList(List<MultipartFile> imageFileList, ReviewForm reviewForm);
}
