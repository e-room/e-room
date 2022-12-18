package com.project.Project.aws.s3;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReviewImagePackageMetaMeta extends FilePackageMeta {

    private Long buildingId;
    private Long roomId;
    private String uuid;

    public ReviewImagePackageCommand createCommand() {
        return new ReviewImagePackageCommand(this);
    }
}
