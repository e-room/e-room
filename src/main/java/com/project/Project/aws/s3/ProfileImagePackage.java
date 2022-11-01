package com.project.Project.aws.s3;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProfileImagePackage implements ImagePackage {

    private Long memberId;

    @Override
    public AmazonS3FolderCommand createCommand() {
        return new ProfileImageFolder(this);
    }
}
