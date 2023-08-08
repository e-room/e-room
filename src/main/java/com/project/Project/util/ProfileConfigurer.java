package com.project.Project.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component(value = "ProfileConfigurer")
@RequiredArgsConstructor
public class ProfileConfigurer {
    // Constants
    private final List<String> onProfileList = List.of("dev", "prod", "test", "local", "localDev", "localProd", "test");

    // Dependencies
    private final Environment environment;

    // Variables
    private String activeProfile;

    // Properties
    private String bucket;
    private String distributionDomain;

    private static String staticBucket;
    private static String staticDistributionDomain;

    @PostConstruct
    public void init() {
        List<String> activeProfileList = List.of(environment.getActiveProfiles());
        for(String onProfile : onProfileList) {
            if (activeProfileList.contains(onProfile)) {
                activeProfile = onProfile;
                break;
            }
        }
        if(activeProfile.equals("dev") || activeProfile.equals("localDev")) {
            bucket = environment.getProperty("cloud.aws.s3.dev-bucket");
            distributionDomain = environment.getProperty("cloud.aws.cloudFront.dev-distributionDomain");
        } else {
            bucket = environment.getProperty("cloud.aws.s3.bucket");
            distributionDomain = environment.getProperty("cloud.aws.cloudFront.distributionDomain");
        }

        this.staticBucket = this.bucket;
        this.staticDistributionDomain = this.distributionDomain;
    }

    public static String getBucket() {
        return staticBucket;
    }

    public static String getDistributionDomain() {
        return staticDistributionDomain;
    }
}
