package com.project.Project.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.Project.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class AmazonS3Service implements FileService {

    private final AmazonS3 amazonS3;
    private final AmazonS3Config amazonS3Config;

    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3.putObject(new PutObjectRequest(amazonS3Config.getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicReadWrite));
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(amazonS3Config.getBucket(), fileName));
    }

    // FIXME: Cloud Front URL
    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(amazonS3Config.getBucket(), fileName).toString();
    }

    public String getFileFolder(AmazonS3FolderCommand command) {
        return command.getFolder();
    }
}
