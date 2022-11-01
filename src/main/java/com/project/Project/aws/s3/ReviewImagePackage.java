package com.project.Project.aws.s3;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReviewImagePackage implements ImagePackage {

    private Long buildingId;
    private Long roomId;

    public ReviewImageFolder createCommand() {
        return new ReviewImageFolder(this);
    }
}
