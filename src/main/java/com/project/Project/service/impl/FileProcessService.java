package com.project.Project.service.impl;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.Project.domain.enums.FileFolder;
import com.project.Project.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileProcessService {
    private final FileService amazonS3Service;

    public String uploadImage(MultipartFile file, FileFolder fileFolder) {
        String fileName = amazonS3Service.getFileFolder(fileFolder) + createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException ioe) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생했습니다 (%s)", file.getOriginalFilename()));
        }

        return amazonS3Service.getFileUrl(fileName);
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public void deleteImage(String url) {
        amazonS3Service.deleteFile(getFileName(url));
    }

    private String getFileName(String url) {
        String[] paths = url.split("/");
        return paths[paths.length-2] + "/" + paths[paths.length-1];
    }
}
