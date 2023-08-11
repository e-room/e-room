package com.project.Project.common.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.Project.common.aws.s3.command.AmazonS3PackageCommand;
import com.project.Project.config.AmazonConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class AmazonS3Service implements FileService {

    private final AmazonS3 amazonS3;
    private final AmazonConfig amazonConfig;

    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicReadWrite));
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(amazonConfig.getBucket(), fileName));
    }

    // return: Cloud Front URL
    public String getFileUrl(String path) {
        String bucketPath = amazonS3.getUrl(amazonConfig.getBucket(), path).toString();
        String cloudFrontDomain = amazonConfig.getDistributionDomain();
        String bucketName = amazonConfig.getBucket();
        String S3BaseURI = "s3://" + bucketName;
        String fileUrl = cloudFrontDomain + "/" + path;
        return fileUrl;
    }

    public String getFileFolder(AmazonS3PackageCommand command) {
        return command.getFolder();
    }
}
