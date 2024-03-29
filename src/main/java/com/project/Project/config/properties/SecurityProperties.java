package com.project.Project.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "login")
public class SecurityProperties {

    private String scheme;
    private String defaultHost;
    private Integer port;
    private String defaultFailurePath;
    private String defaultSuccessPath;
}
