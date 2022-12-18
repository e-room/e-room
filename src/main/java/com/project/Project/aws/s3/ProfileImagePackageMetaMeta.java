package com.project.Project.aws.s3;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProfileImagePackageMetaMeta extends FilePackageMeta {

    private Long memberId;

    @Override
    public AmazonS3PackageCommand createCommand() {
        return new ProfileImagePackageCommand(this);
    }
}
