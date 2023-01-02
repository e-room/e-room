package com.project.Project.aws.s3;

import com.project.Project.domain.Uuid;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewImagePackageMetaMeta extends FilePackageMeta {

    private Long buildingId;
    private Long roomId;
    private Uuid uuidEntity;


    @Builder
    public ReviewImagePackageMetaMeta(String uuid, Long buildingId, Long roomId, Uuid uuidEntity) {
        super(uuid);
        this.buildingId = buildingId;
        this.roomId = roomId;
        this.uuidEntity = uuidEntity;
    }

    public ReviewImagePackageCommand createCommand() {
        return new ReviewImagePackageCommand(this);
    }
}
