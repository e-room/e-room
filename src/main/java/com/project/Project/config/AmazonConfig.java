package com.project.Project.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.project.Project.common.util.ProfileConfigurer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;

@Configuration
@DependsOn(value = { "ProfileConfigurer" })
@Getter
public class AmazonConfig {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.folder.reviewImages}")
    private String reviewImagesFolder;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.cloudFront.distributionDomain}")
    private String distributionDomain;

    private AWSCredentials awsCredentials;

    @PostConstruct
    public void init() {
        awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        bucket = ProfileConfigurer.getBucket();
        distributionDomain = ProfileConfigurer.getDistributionDomain();
        System.out.println("BUCKET : " + bucket);
        System.out.println("DOMAIN : " + distributionDomain);
    }

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSStaticCredentialsProvider(awsCredentials);
    }
}
