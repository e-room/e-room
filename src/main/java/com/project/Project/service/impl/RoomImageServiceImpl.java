package com.project.Project.service.impl;

import com.project.Project.domain.enums.FileFolder;
import com.project.Project.domain.review.ReviewForm;
import com.project.Project.domain.review.RoomImage;
import com.project.Project.repository.RoomImageRepository;
import com.project.Project.service.RoomImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomImageServiceImpl implements RoomImageService {

    private final FileProcessService fileProcessService;
    private final RoomImageRepository roomImageRepository;

    @Transactional
    public void saveImageList(List<MultipartFile> imageFileList, ReviewForm reviewForm) {
        for(MultipartFile multipartFile : imageFileList) {
            String url = fileProcessService.uploadImage(multipartFile, FileFolder.REVIEW_IMAGES);
            RoomImage roomImage = RoomImage.builder().url(url).build();
                roomImage.setReviewForm(reviewForm);
            roomImageRepository.save(roomImage);
        }
    }
}
