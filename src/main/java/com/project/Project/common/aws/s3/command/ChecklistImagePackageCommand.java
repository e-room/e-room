package com.project.Project.common.aws.s3.command;

import com.project.Project.common.aws.s3.metadata.ChecklistImageMetadata;
import com.project.Project.common.util.PropertyUtil;

public class ChecklistImagePackageCommand implements AmazonS3PackageCommand {

    private ChecklistImageMetadata checklistImagePackageMeta;

    public ChecklistImagePackageCommand(ChecklistImageMetadata checklistImagePackageMeta) {
        this.checklistImagePackageMeta = checklistImagePackageMeta;
    }

    public void setReviewImagePackage(ChecklistImageMetadata checklistImagePackageMeta) {
        this.checklistImagePackageMeta = checklistImagePackageMeta;
    }

    @Override
    public String getFolder() {
        return getFolderInternal(this.checklistImagePackageMeta);
    }

    private String getFolderInternal(ChecklistImageMetadata checklistImagePackageMeta) {
        String rootPackage = PropertyUtil.getProperty("cloud.aws.s3.folder.checklistImages");
        // review-images/buildingId/review/${reviewImage file name} 형식으로
        return rootPackage + "/" + checklistImagePackageMeta.getChecklistId() + "/";
    }
}
