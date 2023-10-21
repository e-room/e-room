package com.project.Project.common.aws.s3.metadata;

import com.project.Project.common.aws.s3.command.AmazonS3PackageCommand;
import com.project.Project.common.aws.s3.command.ThumbnailPackageCommand;
import com.project.Project.domain.Uuid;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ThumbnailMetadata extends FileMetadata {

    private LocalDateTime createdAt;
    private String fileName;
    private Uuid uuidEntity;

    @Builder
    public ThumbnailMetadata(String uuid, LocalDateTime createdAt, String fileName, Uuid uuidEntity) {
        super(uuid);
        this.createdAt = createdAt;
        this.fileName = fileName;
        this.uuidEntity = uuidEntity;
    }


    @Override
    public AmazonS3PackageCommand createCommand() {
        return new ThumbnailPackageCommand(this);
    }
}
