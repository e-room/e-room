package com.project.Project.common.aws.s3;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileImagePackageMetaMeta extends FilePackageMeta {

    private Long memberId;

    @Builder
    public ProfileImagePackageMetaMeta(String uuid, Long memberId) {
        super(uuid);
        this.memberId = memberId;
    }

    @Override
    public AmazonS3PackageCommand createCommand() {
        return new ProfileImagePackageCommand(this);
    }
}
