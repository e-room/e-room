package com.project.Project.common.aws.s3.command;

import com.project.Project.common.aws.s3.metadata.ProfileImageMetadata;

public class ProfileImagePackageCommand implements AmazonS3PackageCommand {

    private ProfileImageMetadata profileImagePackageMeta;

    public ProfileImagePackageCommand(ProfileImageMetadata profileImagePackageMeta) {
        this.profileImagePackageMeta = profileImagePackageMeta;
    }

    @Override
    public String getFolder() {
        return getFolderInternal(profileImagePackageMeta);
    }

    private String getFolderInternal(ProfileImageMetadata profileImagePackageMeta) {
        return "";
    }
}
