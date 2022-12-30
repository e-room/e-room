package com.project.Project.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private String allowedImageExtension;
    private Integer thumbnailSize;
    private Integer originMaxSize;
    private String thumbnailLocation;
}

