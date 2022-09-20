package com.project.Project.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.Project.domain.enums.FileFolder;

import java.io.InputStream;

public interface FileService {
    void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName);

    void deleteFile(String fileName);

    String getFileUrl(String fileName);

    String getFileFolder(FileFolder fileFolder);
}
