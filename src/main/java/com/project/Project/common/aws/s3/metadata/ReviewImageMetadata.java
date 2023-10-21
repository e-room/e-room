package com.project.Project.common.aws.s3.metadata;

import com.project.Project.common.aws.s3.command.ReviewImagePackageCommand;
import com.project.Project.domain.Uuid;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewImageMetadata extends FileMetadata {

    private Long buildingId;
    private Uuid uuidEntity;


    @Builder
    public ReviewImageMetadata(String uuid, Long buildingId, Uuid uuidEntity) {
        super(uuid);
        this.buildingId = buildingId;
        this.uuidEntity = uuidEntity;
    }

    public ReviewImagePackageCommand createCommand() {
        return new ReviewImagePackageCommand(this);
    }
}
