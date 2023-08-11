package com.project.Project.common.aws.s3.metadata;

import com.project.Project.common.aws.s3.command.AmazonS3PackageCommand;

public abstract class FileMetadata {
    private String uuid;

    public FileMetadata(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public abstract AmazonS3PackageCommand createCommand();
}
