package com.project.Project.aws.s3;

import com.project.Project.util.PropertyUtil;

public class ReviewImageFolder implements AmazonS3FolderCommand {

    private ReviewImagePackage reviewImagePackage;

    public ReviewImageFolder(ReviewImagePackage reviewImagePackage) {
        this.reviewImagePackage = reviewImagePackage;
    }

    public void setReviewImagePackage(ReviewImagePackage reviewImagePackage) {
        this.reviewImagePackage = reviewImagePackage;
    }

    @Override
    public String getFolder() {
        return getFolderInternal(this.reviewImagePackage);
    }

    private String getFolderInternal(ReviewImagePackage reviewImagePackage) {

        PropertyUtil.getProperty("cloud.aws.s3.folder.reviewImages");
        // review-images/buildingId/roomId/review/${reviewImage file name} 형식으로
        return "";
    }
}
