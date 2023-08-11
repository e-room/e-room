package com.project.Project.common.aws.s3;

public abstract class FilePackageMeta {
    private String uuid;

    public FilePackageMeta(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public abstract AmazonS3PackageCommand createCommand();
}
