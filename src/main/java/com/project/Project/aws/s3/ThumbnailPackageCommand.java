package com.project.Project.aws.s3;

import com.project.Project.util.PropertyUtil;

public class ThumbnailPackageCommand implements AmazonS3PackageCommand {

    private ThumbnailImagePackageMetadata thumbnailImagePackageMetadata;

    public ThumbnailPackageCommand(ThumbnailImagePackageMetadata thumbnailImagePackageMetadata) {
        this.thumbnailImagePackageMetadata = thumbnailImagePackageMetadata;
    }

    public void setReviewImagePackage(ThumbnailImagePackageMetadata thumbnailImagePackageMetadata) {
        this.thumbnailImagePackageMetadata = thumbnailImagePackageMetadata;
    }

    @Override
    public String getFolder() {
        return getFolderInternal(this.thumbnailImagePackageMetadata);
    }

    private String getFolderInternal(ThumbnailImagePackageMetadata thumbnailImagePackageMetadata) {

        String rootPackage = PropertyUtil.getProperty("cloud.aws.s3.folder.thumbnailImages");
        // thumbnail-images/${uuid}/${reviewImage file name} 형식으로
        return rootPackage + "/" + thumbnailImagePackageMetadata.getUuid() + "/";
    }
}
