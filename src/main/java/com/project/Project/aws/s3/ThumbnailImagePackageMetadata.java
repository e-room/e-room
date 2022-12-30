package com.project.Project.aws.s3;

import com.project.Project.domain.Uuid;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ThumbnailImagePackageMetadata extends FilePackageMeta {

    private LocalDateTime createdAt;
    private String fileName;
    private Uuid uuidEntity;

    @Builder
    public ThumbnailImagePackageMetadata(String uuid, LocalDateTime createdAt, String fileName, Uuid uuidEntity) {
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
