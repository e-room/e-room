package com.project.Project.repository;

import com.project.Project.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
}
