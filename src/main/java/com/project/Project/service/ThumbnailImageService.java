package com.project.Project.service;

import com.project.Project.domain.Thumbnail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ThumbnailImageService {
    List<Thumbnail> saveThumbnailList(List<MultipartFile> imageFileList);

}
