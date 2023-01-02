package com.project.Project.repository.member;

import com.project.Project.domain.member.ProfileImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Page<ProfileImage> findAll(Pageable pageable);
}
