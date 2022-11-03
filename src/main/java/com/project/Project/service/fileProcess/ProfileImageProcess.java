package com.project.Project.service.fileProcess;

import com.project.Project.aws.s3.FileService;
import com.project.Project.aws.s3.ProfileImagePackageMetaMeta;
import org.springframework.beans.factory.annotation.Autowired;

public class ProfileImageProcess extends FileProcessServiceImpl<ProfileImagePackageMetaMeta> {
    @Autowired
    public ProfileImageProcess(FileService amazonS3Service) {
        super(amazonS3Service);
    }
}
