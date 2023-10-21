package com.project.Project.common.aws.s3.metadata;

import com.project.Project.common.aws.s3.command.AmazonS3PackageCommand;
import com.project.Project.common.aws.s3.command.ChecklistImagePackageCommand;
import com.project.Project.domain.Uuid;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChecklistImageMetadata extends FileMetadata {


    private Long checklistId;
    private Uuid uuidEntity;


    @Builder
    public ChecklistImageMetadata(String uuid, Long checklistId, Uuid uuidEntity) {
        super(uuid);
        this.checklistId = checklistId;
        this.uuidEntity = uuidEntity;
    }

    @Override
    public AmazonS3PackageCommand createCommand() {
        return new ChecklistImagePackageCommand(this);
    }
}
