package com.project.Project.common.aws.s3;

public class ProfileImagePackageCommand implements AmazonS3PackageCommand {

    private ProfileImagePackageMetaMeta profileImagePackageMeta;

    public ProfileImagePackageCommand(ProfileImagePackageMetaMeta profileImagePackageMeta) {
        this.profileImagePackageMeta = profileImagePackageMeta;
    }

    @Override
    public String getFolder() {
        return getFolderInternal(profileImagePackageMeta);
    }

    private String getFolderInternal(ProfileImagePackageMetaMeta profileImagePackageMeta) {
        return "";
    }
}
