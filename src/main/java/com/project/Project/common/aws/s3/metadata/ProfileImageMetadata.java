package com.project.Project.common.aws.s3.metadata;

import com.project.Project.common.aws.s3.command.AmazonS3PackageCommand;
import com.project.Project.common.aws.s3.command.ProfileImagePackageCommand;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileImageMetadata extends FileMetadata {

    private Long memberId;

    @Builder
    public ProfileImageMetadata(String uuid, Long memberId) {
        super(uuid);
        this.memberId = memberId;
    }

    @Override
    public AmazonS3PackageCommand createCommand() {
        return new ProfileImagePackageCommand(this);
    }
}
