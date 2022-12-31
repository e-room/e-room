package com.project.Project.aws.s3;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewImagePackageMetaMeta extends FilePackageMeta {

    private Long buildingId;
    private Long roomId;

    @Builder
    public ReviewImagePackageMetaMeta(String uuid, Long buildingId, Long roomId) {
        super(uuid);
        this.buildingId = buildingId;
        this.roomId = roomId;
    }

    public ReviewImagePackageCommand createCommand() {
        return new ReviewImagePackageCommand(this);
    }
}
