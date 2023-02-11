package com.project.Project.aws.s3;

import com.project.Project.util.PropertyUtil;

public class ReviewImagePackageCommand implements AmazonS3PackageCommand {

    private ReviewImagePackageMetaMeta reviewImagePackageMeta;

    public ReviewImagePackageCommand(ReviewImagePackageMetaMeta reviewImagePackageMeta) {
        this.reviewImagePackageMeta = reviewImagePackageMeta;
    }

    public void setReviewImagePackage(ReviewImagePackageMetaMeta reviewImagePackageMeta) {
        this.reviewImagePackageMeta = reviewImagePackageMeta;
    }

    @Override
    public String getFolder() {
        return getFolderInternal(this.reviewImagePackageMeta);
    }

    private String getFolderInternal(ReviewImagePackageMetaMeta reviewImagePackageMeta) {

        String rootPackage = PropertyUtil.getProperty("cloud.aws.s3.folder.reviewImages");
        // review-images/buildingId/review/${reviewImage file name} 형식으로
        return rootPackage + "/" + reviewImagePackageMeta.getBuildingId() + "/";
    }
}
