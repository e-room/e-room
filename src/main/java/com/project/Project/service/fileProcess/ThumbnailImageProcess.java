package com.project.Project.service.fileProcess;

import com.project.Project.aws.s3.FileService;
import com.project.Project.aws.s3.ReviewImagePackageMetaMeta;

public class ThumbnailImageProcess extends FileProcessServiceImpl<ReviewImagePackageMetaMeta> {
    public ThumbnailImageProcess(FileService amazonS3Service) {
        super(amazonS3Service);
    }
}
