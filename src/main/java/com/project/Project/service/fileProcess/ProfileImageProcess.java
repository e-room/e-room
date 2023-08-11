package com.project.Project.service.fileProcess;

import com.project.Project.common.aws.s3.FileService;
import com.project.Project.common.aws.s3.metadata.ProfileImageMetadata;
import com.project.Project.repository.uuid.UuidCustomRepositoryImpl;
import com.project.Project.repository.uuid.UuidRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ProfileImageProcess extends FileProcessServiceImpl<ProfileImageMetadata> {
    @Autowired
    public ProfileImageProcess(FileService amazonS3Service, UuidCustomRepositoryImpl uuidCustomRepository, UuidRepository uuidRepository) {
        super(amazonS3Service, uuidCustomRepository, uuidRepository);
    }

}
