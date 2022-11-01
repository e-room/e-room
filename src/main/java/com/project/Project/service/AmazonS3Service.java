package com.project.Project.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.Project.domain.enums.FileFolder;
import com.project.Project.util.component.AmazonS3Component;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class AmazonS3Service implements FileService {

    private final AmazonS3 amazonS3;
    private final AmazonS3Component amazonS3Component;

    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3.putObject(new PutObjectRequest(amazonS3Component.getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicReadWrite));
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(amazonS3Component.getBucket(), fileName));
    }

    // FIXME: Cloud Front URL
    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(amazonS3Component.getBucket(), fileName).toString();
    }

    public String getFileFolder(FileFolder fileFolder) {
        String folder = "";
        if (fileFolder == FileFolder.REVIEW_IMAGES) {
            folder = amazonS3Component.getReviewImagesFolder();
        }
        return folder;
    }
}
