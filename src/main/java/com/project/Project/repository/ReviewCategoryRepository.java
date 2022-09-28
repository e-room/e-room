package com.project.Project.repository;

import com.project.Project.domain.review.ReviewCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCategoryRepository extends JpaRepository<ReviewCategory, Long> {
}
