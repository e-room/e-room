package com.project.Project.aws.s3;

public class ProfileImageFolder implements AmazonS3FolderCommand {

    private ProfileImagePackage profileImagePackage;

    public ProfileImageFolder(ProfileImagePackage profileImagePackage) {
        this.profileImagePackage = profileImagePackage;
    }

    @Override
    public String getFolder() {
        return getFolderInternal(profileImagePackage);
    }

    private String getFolderInternal(ProfileImagePackage profileImagePackage) {
        return "";
    }
}
