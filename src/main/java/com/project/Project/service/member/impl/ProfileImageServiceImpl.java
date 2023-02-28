package com.project.Project.service.member.impl;

import com.project.Project.domain.member.ProfileImage;
import com.project.Project.repository.member.ProfileImageRepository;
import com.project.Project.service.member.ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {

    private final ProfileImageRepository profileImageRepository;

    @Override
    public ProfileImage random() {
        Long totalCount = profileImageRepository.count();
        int randomIndex = (int) (Math.random() * totalCount);
        Page<ProfileImage> profileImageUrlPage = profileImageRepository.findAll(PageRequest.of(randomIndex, 1));
        return profileImageUrlPage.getContent().get(0);
    }
}
