package com.project.Project.common.aws.s3.command;

import com.project.Project.common.aws.s3.metadata.ReviewImageMetadata;
import com.project.Project.common.util.PropertyUtil;

public class ReviewImagePackageCommand implements AmazonS3PackageCommand {

    private ReviewImageMetadata reviewImagePackageMeta;

    public ReviewImagePackageCommand(ReviewImageMetadata reviewImagePackageMeta) {
        this.reviewImagePackageMeta = reviewImagePackageMeta;
    }

    public void setReviewImagePackage(ReviewImageMetadata reviewImagePackageMeta) {
        this.reviewImagePackageMeta = reviewImagePackageMeta;
    }

    @Override
    public String getFolder() {
        return getFolderInternal(this.reviewImagePackageMeta);
    }

    private String getFolderInternal(ReviewImageMetadata reviewImagePackageMeta) {

        String rootPackage = PropertyUtil.getProperty("cloud.aws.s3.folder.reviewImages");
        // review-images/buildingId/review/${reviewImage file name} 형식으로
        return rootPackage + "/" + reviewImagePackageMeta.getBuildingId() + "/";
    }
}
