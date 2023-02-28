package com.project.Project.aws.s3;

import com.project.Project.domain.Uuid;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewImagePackageMetaMeta extends FilePackageMeta {

    private Long buildingId;
    private Uuid uuidEntity;


    @Builder
    public ReviewImagePackageMetaMeta(String uuid, Long buildingId, Uuid uuidEntity) {
        super(uuid);
        this.buildingId = buildingId;
        this.uuidEntity = uuidEntity;
    }

    public ReviewImagePackageCommand createCommand() {
        return new ReviewImagePackageCommand(this);
    }
}
