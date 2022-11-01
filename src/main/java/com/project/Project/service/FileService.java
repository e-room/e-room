package com.project.Project.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.Project.aws.s3.AmazonS3FolderCommand;

import java.io.InputStream;

public interface FileService {
    void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName);

    void deleteFile(String fileName);

    String getFileUrl(String fileName);

    String getFileFolder(AmazonS3FolderCommand amazonS3FolderCommand);
}
