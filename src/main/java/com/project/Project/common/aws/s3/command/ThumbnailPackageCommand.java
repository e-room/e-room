package com.project.Project.common.aws.s3.command;

import com.project.Project.common.aws.s3.metadata.ThumbnailMetadata;
import com.project.Project.common.util.PropertyUtil;

public class ThumbnailPackageCommand implements AmazonS3PackageCommand {

    private ThumbnailMetadata thumbnailMetadata;

    public ThumbnailPackageCommand(ThumbnailMetadata thumbnailMetadata) {
        this.thumbnailMetadata = thumbnailMetadata;
    }

    public void setReviewImagePackage(ThumbnailMetadata thumbnailMetadata) {
        this.thumbnailMetadata = thumbnailMetadata;
    }

    @Override
    public String getFolder() {
        return getFolderInternal(this.thumbnailMetadata);
    }

    private String getFolderInternal(ThumbnailMetadata thumbnailMetadata) {

        String rootPackage = PropertyUtil.getProperty("cloud.aws.s3.folder.thumbnailImages");
        // thumbnail-images/${uuid}/${reviewImage file name} 형식으로
        return rootPackage + "/" + thumbnailMetadata.getUuid() + "/";
    }
}
