package com.project.Project.aws.s3;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReviewImagePackageMetaMeta implements FilePackageMeta {

    private Long buildingId;
    private Long roomId;

    public ReviewImagePackageCommand createCommand() {
        return new ReviewImagePackageCommand(this);
    }
}
