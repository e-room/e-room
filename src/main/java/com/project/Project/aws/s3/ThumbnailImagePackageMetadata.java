package com.project.Project.aws.s3;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ThumbnailImagePackageMetadata extends FilePackageMeta {

    private LocalDateTime createdAt;
    private String fileName;

    @Override
    public AmazonS3PackageCommand createCommand() {
        return new ThumbnailPackageCommand(this);
    }
}
