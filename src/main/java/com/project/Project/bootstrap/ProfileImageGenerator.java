package com.project.Project.bootstrap;

import com.project.Project.domain.member.ProfileImage;
import com.project.Project.repository.member.ProfileImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 서버 기동 시점에
 * DB에 프로필이미지 URL들이 하나도 없으면
 * 정해진 18개 이미지 Insert 해주는 클래스
 */
@Component
public class ProfileImageGenerator implements CommandLineRunner {

    private final ProfileImageRepository profileImageRepository;

    @Autowired
    public ProfileImageGenerator(ProfileImageRepository profileImageRepository) {
        this.profileImageRepository = profileImageRepository;
    }

    @Value("${cloud.aws.cloudFront.distributionDomain}")
    private String distributionDomain;

    @Value("${cloud.aws.s3.folder.profileImages}")
    private String profileImagesFolder;

    // 색상-눈_eyes-입_mouth.png
    private final String profileImageFileFormat = "%s-%s_eyes-%s_mouth.png";

    private final List<String> colorType = List.of("blue", "green", "yellow");
    private final List<String> eyeType = List.of("round", "smile");
    private final List<String> mouthType = List.of("d", "oval", "u");

    @Override
    public void run(String... args) throws Exception {
        long totalCount = profileImageRepository.count();
        if (totalCount > 0) return;

        List<ProfileImage> profileImageList = new ArrayList<>();

        for (int i = 0; i < colorType.size(); i++)
            for (int j = 0; j < eyeType.size(); j++)
                for (int k = 0; k < mouthType.size(); k++) {
                    String profileImageFile = String.format(profileImageFileFormat, colorType.get(i), eyeType.get(j), mouthType.get(k));
                    String profileImageUrl = distributionDomain + "/" + profileImagesFolder + "/" + profileImageFile;
                    profileImageList.add(ProfileImage.builder()
                            .url(profileImageUrl)
                            .build());
                }

        profileImageRepository.saveAll(profileImageList);
    }
}
